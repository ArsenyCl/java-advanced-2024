package info.kgeorgiy.ja.Presniakov_Arsenii.crawler;

import info.kgeorgiy.java.advanced.crawler.Crawler;
import info.kgeorgiy.java.advanced.crawler.Document;
import info.kgeorgiy.java.advanced.crawler.Downloader;
import info.kgeorgiy.java.advanced.crawler.Result;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;


public class WebCrawler implements Crawler {

    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors = Executors.newFixedThreadPool(extractors);
    }

    @Override
    public Result download(String url, int depth) {
        Set<String> downloaded = ConcurrentHashMap.newKeySet();
        Set<String> toDownloadNext = ConcurrentHashMap.newKeySet();
        Map<String, IOException> errors = new ConcurrentHashMap<>();

        toDownloadNext.add(url);
        for (int i = 0; i < depth; i++) {
            downloadLayer(depth, i, downloaded, toDownloadNext, errors);
        }
        return new Result(new ArrayList<>(downloaded), errors);
    }

    @Override
    public void close() {
        extractors.close();
        downloaders.close();
    }

    private void downloadLayer(int maxDepth, int curDepth, Set<String> downloaded,
                               Set<String> toDownloadNext,
                               Map<String, IOException> errors) {

        List<String> toDownload = new ArrayList<>(toDownloadNext);
        ReadyToReturn waitFor = new ReadyToReturn(toDownload.size());
        toDownloadNext.clear();
        IntStream.range(0, toDownload.size()).forEach(i -> {
            String url = toDownload.get(i);
            if (downloaded.contains(url)) {
                return;
            }
            downloaders.submit(() -> {
                try {
                    Document document = downloader.download(url);
                    downloaded.add(url);
                    if (curDepth < maxDepth - 1) {
                        extractors.submit(() -> {
                            try {
                                toDownloadNext.addAll(document.extractLinks());
                                waitFor.ready(i);
                            } catch (IOException e) {
                                errors.put(url, e);
                                waitFor.ready(i);
                            }
                        });
                    }
                } catch (IOException e) {
                    errors.put(url, e);
                    waitFor.ready(i);
                }
            });
        });

        try {
            waitFor.waitUntilReady();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class ReadyToReturn {
    private final List<Boolean> ready;

    public ReadyToReturn(int n) {
        ready = new ArrayList<>(Collections.nCopies(n, false));
    }

    public synchronized void waitUntilReady() throws InterruptedException {
        while (!ready.stream().allMatch(Boolean::booleanValue)) {
            wait();
        }
    }

    public void ready(int n) {
        ready.set(n, true);
        if (ready.stream().allMatch(Boolean::booleanValue)) {
            synchronized (this) {
                notify();
            }
        }
    }
}
