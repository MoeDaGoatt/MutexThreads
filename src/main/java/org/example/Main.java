package org.example;

import java.util.concurrent.locks.ReentrantLock;

public class Main {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Thread writer = new WriterThread(resource);
        Thread reader = new ReaderThread(resource);

        writer.start();
        reader.start();

        try {
            writer.join();
            reader.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class SharedResource {
        private int sharedData = 0;
        private final ReentrantLock lock = new ReentrantLock();

        public void writeData(int value) {
            lock.lock();
            try {
                sharedData = value;
                System.out.println("Writer: Wrote " + sharedData);
            } finally {
                lock.unlock();
            }
        }

        public void readData() {
            lock.lock();
            try {
                System.out.println("Reader: Read " + sharedData);
            } finally {
                lock.unlock();
            }
        }
    }

    static class WriterThread extends Thread {
        private final SharedResource resource;

        public WriterThread(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                resource.writeData(i * 10);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class ReaderThread extends Thread {
        private final SharedResource resource;

        public ReaderThread(SharedResource resource) {
            this.resource = resource;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 5; i++) {
                resource.readData();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
