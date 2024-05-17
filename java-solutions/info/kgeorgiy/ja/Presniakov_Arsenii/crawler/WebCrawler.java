package info.kgeorgiy.ja.Presniakov_Arsenii.crawler;

import info.kgeorgiy.java.advanced.crawler.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.IntStream;


public class WebCrawler implements Crawler, NewCrawler {

    private final Downloader downloader;
    private final ExecutorService downloaders;
    private final ExecutorService extractors;

    public static void main(String[] args) {
        if (args == null || args.length < 1 || args.length > 5 || Arrays.stream(args).allMatch(Objects::nonNull)) {
            System.err.println("Incorrect args(null or incorrect size or one of args is null)");
            return;
        }

        Result result;
        try (var crawler = new WebCrawler(
                new CachingDownloader(5000),
                intOrDefault(args, 2, 1),
                intOrDefault(args, 3, 1),
                intOrDefault(args, 4, Integer.MAX_VALUE)
        )) {
            result = crawler.download(args[0], intOrDefault(args, 1, 1));
        } catch (IOException e) {
            System.err.println("Downloader problem: " + e.getMessage());
            return;
        } catch (NumberFormatException e) {
            System.err.println("One of arguments except of 1 is not an integer: " + e.getMessage());
            return;
        }

        System.out.println("Downloaded successfully:");
        for (var url : result.getDownloaded()) {
            System.out.println("link: " + url);
        }
    }

    private static int intOrDefault(String[] args, int index, int defaultValue) {
        return args.length > index ? Integer.parseInt(args[index]) : defaultValue;
    }

    public WebCrawler(Downloader downloader, int downloaders, int extractors, int perHost) {
        this.downloader = downloader;
        this.downloaders = Executors.newFixedThreadPool(downloaders);
        this.extractors = Executors.newFixedThreadPool(extractors);
    }

    @Override
    public Result download(String url, int depth, Set<String> excludes) {
        Set<String> downloaded = ConcurrentHashMap.newKeySet();
        Set<String> cache = ConcurrentHashMap.newKeySet();
        Set<String> toDownloadNext = ConcurrentHashMap.newKeySet();
        Map<String, IOException> errors = new ConcurrentHashMap<>();

        toDownloadNext.add(url);
        IntStream.range(0, depth+1).forEach(i -> {
            downloadLayer(depth, i, downloaded, cache, toDownloadNext, errors, excludes);
        });

        return new Result(new ArrayList<>(downloaded), errors);

    }

    @Override
    public Result download(String url, int depth) {
        return download(url, depth, new HashSet<>());
    }

    @Override
    public void close() {
        extractors.close();
        downloaders.close();
    }

    private void downloadLayer(int maxDepth, int curDepth, Set<String> downloaded, Set<String> cache,
                               Set<String> toDownloadNext,
                               Map<String, IOException> errors, Set<String> excludes) {

        List<String> toDownload = new ArrayList<>(toDownloadNext);
        Phaser phaser = new Phaser(1);
        toDownloadNext.clear();
        for (String url : toDownload) {

            if (!cache.add(url) || !notInExcludes(excludes, url)) {
                continue;
            }

            phaser.register();
            downloaders.submit(() -> {
                try {

                    Document document = downloader.download(url);
                    downloaded.add(url);

                    if (curDepth < maxDepth - 1) {
                        phaser.register();
                        extractors.submit(() -> {
                            try {
                                toDownloadNext.addAll(document.extractLinks());
                            } catch (IOException e) {
                                errors.put(url, e);
                            } finally {
                                phaser.arrive();
                            }
                        });
                    }
                } catch (IOException e) {
                    errors.put(url, e);
                } finally {
                    phaser.arrive();
                }
            });
        }
        phaser.arriveAndAwaitAdvance();
    }

    private boolean notInExcludes(Set<String> excludes, String url) {
        return excludes.stream().noneMatch(url::contains);
    }
}
