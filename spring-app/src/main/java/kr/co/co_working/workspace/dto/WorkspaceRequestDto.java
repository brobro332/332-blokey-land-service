package kr.co.co_working.workspace.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class WorkspaceRequestDto {
    @Getter
    @Setter
    public static class CREATE {
        private String name;
        private String description;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class READ {
        private String memberEmail;
        private String memberName;
        private Long id;
        private String name;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class UPDATE {
        private String name;
        private String description;
        private String leader;

        @Builder
        public UPDATE(String name, String description, String leader) {
            this.name = name;
            this.description = description;
            this.leader = leader;
        }
    }
}
