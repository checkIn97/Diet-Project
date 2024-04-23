function scrollToMembership() {
    var membershipArticle = document.getElementById("membership");
    if (membershipArticle) {
        // 회원가입 섹션으로 부드럽게 스크롤
        membershipArticle.scrollIntoView({ behavior: 'smooth' });
    }
}
