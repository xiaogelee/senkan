package com.squareup.okhttp;

import com.squareup.okhttp.internal.http.HeaderParser;
import java.util.concurrent.TimeUnit;

public final class CacheControl {
    public static final CacheControl FORCE_CACHE = new Builder().onlyIfCached().maxStale(Integer.MAX_VALUE, TimeUnit.SECONDS).build();
    public static final CacheControl FORCE_NETWORK = new Builder().noCache().build();
    private final boolean isPublic;
    private final int maxAgeSeconds;
    private final int maxStaleSeconds;
    private final int minFreshSeconds;
    private final boolean mustRevalidate;
    private final boolean noCache;
    private final boolean noStore;
    private final boolean noTransform;
    private final boolean onlyIfCached;
    private final int sMaxAgeSeconds;

    public static final class Builder {
        int maxAgeSeconds = -1;
        int maxStaleSeconds = -1;
        int minFreshSeconds = -1;
        boolean noCache;
        boolean noStore;
        boolean noTransform;
        boolean onlyIfCached;

        public Builder noCache() {
            this.noCache = true;
            return this;
        }

        public Builder noStore() {
            this.noStore = true;
            return this;
        }

        public Builder maxAge(int maxAge, TimeUnit timeUnit) {
            if (maxAge < 0) {
                throw new IllegalArgumentException("maxAge < 0: " + maxAge);
            }
            long maxAgeSecondsLong = timeUnit.toSeconds((long) maxAge);
            this.maxAgeSeconds = maxAgeSecondsLong > 2147483647L ? Integer.MAX_VALUE : (int) maxAgeSecondsLong;
            return this;
        }

        public Builder maxStale(int maxStale, TimeUnit timeUnit) {
            if (maxStale < 0) {
                throw new IllegalArgumentException("maxStale < 0: " + maxStale);
            }
            long maxStaleSecondsLong = timeUnit.toSeconds((long) maxStale);
            this.maxStaleSeconds = maxStaleSecondsLong > 2147483647L ? Integer.MAX_VALUE : (int) maxStaleSecondsLong;
            return this;
        }

        public Builder minFresh(int minFresh, TimeUnit timeUnit) {
            if (minFresh < 0) {
                throw new IllegalArgumentException("minFresh < 0: " + minFresh);
            }
            long minFreshSecondsLong = timeUnit.toSeconds((long) minFresh);
            this.minFreshSeconds = minFreshSecondsLong > 2147483647L ? Integer.MAX_VALUE : (int) minFreshSecondsLong;
            return this;
        }

        public Builder onlyIfCached() {
            this.onlyIfCached = true;
            return this;
        }

        public Builder noTransform() {
            this.noTransform = true;
            return this;
        }

        public CacheControl build() {
            return new CacheControl();
        }
    }

    private CacheControl(boolean noCache, boolean noStore, int maxAgeSeconds, int sMaxAgeSeconds, boolean isPublic, boolean mustRevalidate, int maxStaleSeconds, int minFreshSeconds, boolean onlyIfCached, boolean noTransform) {
        this.noCache = noCache;
        this.noStore = noStore;
        this.maxAgeSeconds = maxAgeSeconds;
        this.sMaxAgeSeconds = sMaxAgeSeconds;
        this.isPublic = isPublic;
        this.mustRevalidate = mustRevalidate;
        this.maxStaleSeconds = maxStaleSeconds;
        this.minFreshSeconds = minFreshSeconds;
        this.onlyIfCached = onlyIfCached;
        this.noTransform = noTransform;
    }

    private CacheControl(Builder builder) {
        this.noCache = builder.noCache;
        this.noStore = builder.noStore;
        this.maxAgeSeconds = builder.maxAgeSeconds;
        this.sMaxAgeSeconds = -1;
        this.isPublic = false;
        this.mustRevalidate = false;
        this.maxStaleSeconds = builder.maxStaleSeconds;
        this.minFreshSeconds = builder.minFreshSeconds;
        this.onlyIfCached = builder.onlyIfCached;
        this.noTransform = builder.noTransform;
    }

    public boolean noCache() {
        return this.noCache;
    }

    public boolean noStore() {
        return this.noStore;
    }

    public int maxAgeSeconds() {
        return this.maxAgeSeconds;
    }

    public int sMaxAgeSeconds() {
        return this.sMaxAgeSeconds;
    }

    public boolean isPublic() {
        return this.isPublic;
    }

    public boolean mustRevalidate() {
        return this.mustRevalidate;
    }

    public int maxStaleSeconds() {
        return this.maxStaleSeconds;
    }

    public int minFreshSeconds() {
        return this.minFreshSeconds;
    }

    public boolean onlyIfCached() {
        return this.onlyIfCached;
    }

    public boolean noTransform() {
        return this.noTransform;
    }

    public static CacheControl parse(Headers headers) {
        boolean noCache = false;
        boolean noStore = false;
        int maxAgeSeconds = -1;
        int sMaxAgeSeconds = -1;
        boolean isPublic = false;
        boolean mustRevalidate = false;
        int maxStaleSeconds = -1;
        int minFreshSeconds = -1;
        boolean onlyIfCached = false;
        boolean noTransform = false;
        int i = 0;
        while (i < headers.size()) {
            if (headers.name(i).equalsIgnoreCase("Cache-Control") || headers.name(i).equalsIgnoreCase("Pragma")) {
                String string = headers.value(i);
                int pos = 0;
                while (pos < string.length()) {
                    String parameter;
                    int tokenStart = pos;
                    pos = HeaderParser.skipUntil(string, pos, "=,;");
                    String directive = string.substring(tokenStart, pos).trim();
                    if (pos == string.length() || string.charAt(pos) == ',' || string.charAt(pos) == ';') {
                        pos++;
                        parameter = null;
                    } else {
                        pos = HeaderParser.skipWhitespace(string, pos + 1);
                        int parameterStart;
                        if (pos >= string.length() || string.charAt(pos) != '\"') {
                            parameterStart = pos;
                            pos = HeaderParser.skipUntil(string, pos, ",;");
                            parameter = string.substring(parameterStart, pos).trim();
                        } else {
                            pos++;
                            parameterStart = pos;
                            pos = HeaderParser.skipUntil(string, pos, "\"");
                            parameter = string.substring(parameterStart, pos);
                            pos++;
                        }
                    }
                    if ("no-cache".equalsIgnoreCase(directive)) {
                        noCache = true;
                    } else if ("no-store".equalsIgnoreCase(directive)) {
                        noStore = true;
                    } else if ("max-age".equalsIgnoreCase(directive)) {
                        maxAgeSeconds = HeaderParser.parseSeconds(parameter, -1);
                    } else if ("s-maxage".equalsIgnoreCase(directive)) {
                        sMaxAgeSeconds = HeaderParser.parseSeconds(parameter, -1);
                    } else if ("public".equalsIgnoreCase(directive)) {
                        isPublic = true;
                    } else if ("must-revalidate".equalsIgnoreCase(directive)) {
                        mustRevalidate = true;
                    } else if ("max-stale".equalsIgnoreCase(directive)) {
                        maxStaleSeconds = HeaderParser.parseSeconds(parameter, Integer.MAX_VALUE);
                    } else if ("min-fresh".equalsIgnoreCase(directive)) {
                        minFreshSeconds = HeaderParser.parseSeconds(parameter, -1);
                    } else if ("only-if-cached".equalsIgnoreCase(directive)) {
                        onlyIfCached = true;
                    } else if ("no-transform".equalsIgnoreCase(directive)) {
                        noTransform = true;
                    }
                }
            }
            i++;
        }
        return new CacheControl(noCache, noStore, maxAgeSeconds, sMaxAgeSeconds, isPublic, mustRevalidate, maxStaleSeconds, minFreshSeconds, onlyIfCached, noTransform);
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        if (this.noCache) {
            result.append("no-cache, ");
        }
        if (this.noStore) {
            result.append("no-store, ");
        }
        if (this.maxAgeSeconds != -1) {
            result.append("max-age=").append(this.maxAgeSeconds).append(", ");
        }
        if (this.sMaxAgeSeconds != -1) {
            result.append("s-maxage=").append(this.sMaxAgeSeconds).append(", ");
        }
        if (this.isPublic) {
            result.append("public, ");
        }
        if (this.mustRevalidate) {
            result.append("must-revalidate, ");
        }
        if (this.maxStaleSeconds != -1) {
            result.append("max-stale=").append(this.maxStaleSeconds).append(", ");
        }
        if (this.minFreshSeconds != -1) {
            result.append("min-fresh=").append(this.minFreshSeconds).append(", ");
        }
        if (this.onlyIfCached) {
            result.append("only-if-cached, ");
        }
        if (this.noTransform) {
            result.append("no-transform, ");
        }
        if (result.length() == 0) {
            return "";
        }
        result.delete(result.length() - 2, result.length());
        return result.toString();
    }
}
