package com.ssafy.mugit.mugitory.fixure;

import com.ssafy.mugit.record.entity.Source;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum SourceFixture {
    SOURCE(null, "https://mugit.site/files/source_1"),
    SOURCE_2(null, "https://mugit.site/files/source_2");
    private final Long id;
    private final String path;

    public Source getFixture() {
        return new Source(this.id, this.path);
    }
}
