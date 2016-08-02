package com.example.madhurarora.treebo.Util;


import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.Executor;

/**
 * Created by madhur.arora on 19/02/16.
 */
abstract public class AbstractAsyncTask<Params, Progress, Result> {
    private final Tracker mtracker;
    private volatile boolean mCancelled;
    private final InnerTask<Params, Progress, Result> mInnerTask;

    private static final Executor SERIAL_EXECUTOR = AsyncTask.SERIAL_EXECUTOR;
    private static final Executor PARALLEL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;

    public AbstractAsyncTask(Tracker tracker) {
        mtracker = tracker;
        if(mtracker != null) {
            mtracker.add(this);
        }
        mInnerTask = new InnerTask<Params, Progress, Result>(this);
    }

    public static class Tracker {
        private final LinkedList<AbstractAsyncTask<?, ?, ?>> mTasks = new LinkedList<AbstractAsyncTask<?, ?, ?>>();

        private void add(AbstractAsyncTask<?, ?, ?> task) {
            synchronized (mTasks) {
                mTasks.remove(task);
            }
        }
        public void remove(AbstractAsyncTask<?, ?, ?> task) {
            synchronized (mTasks) {
                mTasks.remove(task);
            }
        }

        public void cancelAllInterrupt() {
            synchronized (mTasks) {
                for(AbstractAsyncTask<?, ?, ?> task : mTasks) {
                    task.cancel(true);
                }
                mTasks.clear();
            }
        }

        void cancelOthers(AbstractAsyncTask<?, ?, ?> current) {
            final Class<?> clazz = current.getClass();
            synchronized (mTasks) {
                final ArrayList<AbstractAsyncTask<?, ?, ?>> toRemove = new ArrayList<AbstractAsyncTask<?, ?, ?>>();
                for(AbstractAsyncTask<?, ?, ?> task : mTasks) {
                    if((task != current) && task.getClass().equals(clazz)) {
                        task.cancel(true);
                        toRemove.add(task);
                    }
                }
                for(AbstractAsyncTask<?, ?, ?> task : toRemove) {
                    mTasks.remove(task);
                }
            }
        }

        int getTaskCountForTest() { return mTasks.size(); }

        boolean containsTaskForTest(AbstractAsyncTask<?, ?, ?> task) {
            return mTasks.contains(task);
        }

    }

    protected abstract Result doInBackground(Params... params);

    final void unregisterSelf() {
        if(mtracker != null) {
            mtracker.remove(this);
        }
    }

    protected void onCancelled(Result result) { }

    protected void onSuccess(Result result) { }

    public static class InnerTask<Params2, Progress2, Result2> extends AsyncTask<Params2, Progress2, Result2> {
        private final AbstractAsyncTask<Params2, Progress2, Result2> mOwner;
        private InnerTask(AbstractAsyncTask<Params2, Progress2, Result2> owner) { mOwner = owner; }

        @Override
        protected Result2 doInBackground(Params2... params) {
            return mOwner.doInBackground(params);
        }

        @Override
        protected void onCancelled(Result2 result2) {
            mOwner.unregisterSelf();
            mOwner.onCancelled(result2);
        }

        @Override
        protected void onPostExecute(Result2 result2) {
            mOwner.unregisterSelf();
            if(mOwner.mCancelled) {
                mOwner.onCancelled(result2);
            }
            else {
                mOwner.onSuccess(result2);
            }
        }
    }

    public final void cancel(boolean mayInterruptIfRunning) {
        mCancelled = true;
        mInnerTask.cancel(mayInterruptIfRunning);
    }

    public static AbstractAsyncTask<Void, Void, Void> runAsyncParallel(Runnable runnable) {
        return runAsyncInternal(PARALLEL_EXECUTOR, runnable);
    }

    private static AbstractAsyncTask<Void,Void,Void> runAsyncInternal(Executor executor, final Runnable runnable) {
        AbstractAsyncTask<Void,Void,Void> task = new AbstractAsyncTask<Void, Void, Void>(null) {
            @Override
            protected Void doInBackground(Void... params) {
                runnable.run();
                return null;
            }
        };
        return task.executeInternal(executor, false, (Void[]) null);
    }

    private final AbstractAsyncTask<Params ,Progress ,Result> executeInternal (Executor executor,
                                                                               boolean cancelPrevious, Params... params) {
        if(cancelPrevious) {
            if(mtracker == null) {
                throw  new IllegalStateException();
            } else {
                mtracker.cancelOthers(this);
            }
        }
        mInnerTask.executeOnExecutor(executor, params);
        return  this;
    }
}
