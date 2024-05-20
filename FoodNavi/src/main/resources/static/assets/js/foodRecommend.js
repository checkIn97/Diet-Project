// 모든 .section 요소를 선택
var sections = document.querySelectorAll('.foodRecommend .section');

// 각 .section 요소에 대해 이벤트 리스너를 추가
sections.forEach(function (section) {
    // 마우스가 .section 위에 있을 때
    section.addEventListener('mouseover', function () {
        // 다른 .section 요소들에 블러 효과를 적용
        sections.forEach(function (otherSection) {
            if (otherSection !== section) {
                otherSection.style.filter = 'blur(2px)';
            }
        });
    });

    // 마우스가 .section을 떠났을 때
    section.addEventListener('mouseout', function () {
        // 모든 .section 요소의 블러 효과를 제거
        sections.forEach(function (otherSection) {
            otherSection.style.filter = 'none';
        });
    });
});

// section1 요소를 선택
var section1 = document.querySelector('#section1');

// 화살표 이미지 요소를 선택
var arrowL = document.querySelector('#section1 #arrowL');
var arrowR = document.querySelector('#section1 #arrowR');

// section1 요소에 마우스가 hover 되었을 때
section1.addEventListener('mouseover', function() {
    // 화살표 이미지를 표시
    arrowL.style.display = 'block';
    arrowR.style.display = 'block';
});

// section1 요소에서 마우스가 떠났을 때
section1.addEventListener('mouseout', function() {
    // 화살표 이미지를 숨김
    arrowL.style.display = 'none';
    arrowR.style.display = 'none';
});

var sections = document.getElementsByClassName('section');
for (var i = 0; i < sections.length; i++) {
    sections[i].addEventListener('click', function() {
        var checkmark = this.querySelector('.checkmark');
        if (this.dataset.clicked === 'true') {
            this.style.backgroundColor = ''; // 배경색을 원래대로 복원
            if (checkmark) {
                checkmark.style.display = 'none'; // 이미지를 숨김
            }
            this.dataset.clicked = 'false';
        } else {
            this.style.backgroundColor = 'rgba(255, 255, 255, 0.5)'; // 흰색으로 덮어씌움
            if (checkmark) {
                checkmark.style.display = 'block'; // 이미지를 표시
            }
            this.dataset.clicked = 'true';
        }
    });
}
