describe("접속 및 데이터 초기화 테스트", () => {
  it("사이트 접속", () => {
    cy.visit("/");
  });

  it("데이터 초기화", () => {
    {
      /* 로그인 */
    }
    cy.visit("/login");

    cy.get('input[placeholder="이메일"]').type("test1@test.com");
    cy.get('input[placeholder="비밀번호"]').type("test");
    cy.contains("button", "로그인").click();

    cy.url().should("not.include", "/login");
    cy.contains("환영합니다.");

    {
      /* 프로젝트 생성 */
    }
    cy.contains("button", "프로젝트").click();
    cy.contains("div", "새 프로젝트 만들기").click();

    cy.get('input[placeholder="프로젝트 제목"]').type("테스트 프로젝트");
    cy.get('textarea[placeholder="프로젝트 설명"]').type(
      "테스트 프로젝트 설명"
    );

    cy.contains("label", "시작예정일")
      .next("input")
      .type(startDate.toISOString().slice(0, 10));
    cy.contains("label", "종료예정일")
      .next("input")
      .type(endDate.toISOString().slice(0, 10));
    cy.contains("label", "실제시작일")
      .next("input")
      .type(startDate.toISOString().slice(0, 10));
    cy.contains("label", "실제종료일")
      .next("input")
      .type(endDate.toISOString().slice(0, 10));

    cy.contains("button", "생성").click();

    cy.contains("h3", "테스트 프로젝트").click();

    {
      /* 태스크 생성 */
    }
    cy.contains("button", "새 태스크 만들기").click();
    cy.get('input[placeholder="태스크 제목"]').type("테스트 태스크");
    cy.get('textarea[placeholder="태스크 설명"]').type("테스트 태스크 설명");

    cy.contains("label", "시작예정일")
      .next("input")
      .type(startDate.toISOString().slice(0, 10));
    cy.contains("label", "종료예정일")
      .next("input")
      .type(endDate.toISOString().slice(0, 10));

    cy.contains("button", "생성").click();

    {
      /* 제안 생성 */
    }
    cy.contains("button", "제안").click();
    cy.contains("button", "사용자 목록").click();

    cy.contains("td", "테스터2")
      .parent("tr")
      .within(() => {
        cy.contains("button", "제안 보내기").then(($btn) => {
          if ($btn.length) {
            cy.wrap($btn).click();
          }
        });
      });

    cy.contains("button", "닫기").click();

    {
      /* 마일스톤 생성 */
    }
    cy.contains("button", "마일스톤").click();

    cy.contains("div.text-sm.font-semibold", "1").closest("div.border").click();

    cy.contains("div", "새 마일스톤 만들기").click();

    cy.contains("button", "프로젝트 선택").click();

    cy.get('[role="listbox"]')
      .should("be.visible")
      .within(() => {
        cy.contains("div.cursor-pointer", "테스트 프로젝트", { timeout: 5000 })
          .should("be.visible")
          .click({ force: true });
      });

    cy.get('input[placeholder="마일스톤 제목"]').type("테스트 마일스톤");
    cy.get('textarea[placeholder="마일스톤 설명"]').type(
      "테스트 마일스톤 설명"
    );

    cy.contains("button", "생성").click();

    {
      /* 대시보드 확인 */
    }
    cy.contains("button", "대시보드").click();
  });
});

const today = new Date();

function formatDate(date) {
  const y = date.getFullYear();
  const m = String(date.getMonth() + 1).padStart(2, "0");
  const d = String(date.getDate()).padStart(2, "0");
  return `${y}-${m}-${d}`;
}

const startDate = new Date(today);
startDate.setDate(today.getDate() - 5);

const endDate = new Date(today);
endDate.setDate(today.getDate() + 5);
