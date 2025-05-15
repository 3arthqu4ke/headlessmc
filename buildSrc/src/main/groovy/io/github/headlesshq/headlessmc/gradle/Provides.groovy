package io.github.headlesshq.headlessmc.gradle

class Provides {
    final String service;
    final String[] with

    Provides(String service, String... with) {
        this.service = service.replace('\\.', '/')
        this.with = with == null ? null : Arrays.stream(with as String[])
                .map(s -> s.replace('\\.', '/'))
                .toArray(String[]::new)
    }

}
