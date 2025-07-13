describe("회원가입 테스트", () => {
  it("회원가입", () => {
    cy.visit("/");

    cy.on("window:alert", () => {});

    cy.contains("button", "회원가입").click();

    cy.get('input[placeholder="이메일"]').type("test1@test.com");
    cy.get('input[placeholder="비밀번호"]').type("test");
    cy.get('input[placeholder="닉네임"]').type("테스터1");
    cy.get('textarea[placeholder="한 줄 소개"]').type(
      "안녕하세요, 첫 번째 테스터입니다."
    );
    cy.contains("button", "회원가입").click();

    cy.wait(1000);

    cy.contains("button", "회원가입").click();

    cy.get('input[placeholder="이메일"]').type("test2@test.com");
    cy.get('input[placeholder="비밀번호"]').type("test");
    cy.get('input[placeholder="닉네임"]').type("테스터2");
    cy.get('textarea[placeholder="한 줄 소개"]').type(
      "안녕하세요, 두 번째 테스터입니다."
    );
    cy.contains("button", "회원가입").click();
  });
});
