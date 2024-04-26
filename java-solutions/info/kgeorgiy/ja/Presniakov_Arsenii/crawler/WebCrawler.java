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
        IntStream.range(0, depth).forEach(i -> {
            downloadLayer(depth, i, downloaded, toDownloadNext, errors);
        });

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
        CountDownLatch waitFor = new CountDownLatch(toDownload.size());
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
                            } catch (IOException e) {
                                errors.put(url, e);
                            }
                            waitFor.countDown();
                        });
                    } else {
                        waitFor.countDown();
                    }
                } catch (IOException e) {
                    errors.put(url, e);
                }
            });
        });

        try {
            waitFor.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
