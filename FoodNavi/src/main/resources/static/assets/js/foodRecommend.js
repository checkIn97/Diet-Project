// // section1 요소를 선택
// var section1 = document.querySelector('#section1');
//
// // 화살표 이미지 요소를 선택
// var arrowL = document.querySelector('#section1 #arrowL1');
// var arrowR = document.querySelector('#section1 #arrowR1');
//
// // section1 요소에 마우스가 hover 되었을 때
// section1.addEventListener('mouseover', function() {
//     // 화살표 이미지를 표시
//     arrowL.style.display = 'block';
//     arrowR.style.display = 'block';
// });
//
// // section1 요소에서 마우스가 떠났을 때
// section1.addEventListener('mouseout', function() {
//     // 화살표 이미지를 숨김
//     arrowL.style.display = 'none';
//     arrowR.style.display = 'none';
// });


/*화면 채크 마크 표시*/
var sections = document.getElementsByClassName('section');
for (var i = 0; i < sections.length; i++) {
    sections[i].addEventListener('click', function(event) {
        // 'quantity' 클래스를 가진 요소를 포함하는 'section' 요소를 클릭한 경우 이벤트 핸들러를 실행하지 않음
        var quantity = this.querySelector('.quantity');
        if (quantity && quantity.contains(event.target)) {
            return;
        }

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


/*수량 증가/감소*/
var quantities = document.getElementsByClassName('quantity');

for (var i = 0; i < quantities.length; i++) {
    (function() {
        var decreaseButton = quantities[i].getElementsByClassName('decrease')[0];
        var increaseButton = quantities[i].getElementsByClassName('increase')[0];
        var input = quantities[i].getElementsByClassName('quantity-input')[0];

        decreaseButton.addEventListener('click', function() {
            var currentValue = Number(input.value);
            if (currentValue > 0) {
                input.value = currentValue - 1;
            }
        });

        increaseButton.addEventListener('click', function() {
            var currentValue = Number(input.value);
            input.value = currentValue + 1;
        });
    })();
}


/*슬라이드 기능*/

// 각 섹션에 대한 현재 활성 추천을 초기화
var currentRecommendation = {
    section1: 0,
    section2: 0,
    section3: 0
};

// 각 섹션에 마우스 이벤트 리스너 추가
var sections = document.getElementsByClassName('section');
for (var i = 0; i < sections.length; i++) {
    var section = sections[i];
    var sectionId = section.id;

    // 마우스를 클릭하면 시작 위치를 기록
    section.addEventListener('mousedown', function(e) {
        this.dataset.startX = e.clientX;
        this.dataset.dragging = 'true';
    });

    // 마우스를 움직이면 슬라이드를 이동
    section.addEventListener('mousemove', function(e) {
        if (this.dataset.dragging !== 'true') {
            return;
        }

        var startX = Number(this.dataset.startX);
        var distance = e.clientX - startX;

        // 오른쪽으로 드래그하면 이전 음식 추천으로, 왼쪽으로 드래그하면 다음 음식 추천으로
        if (Math.abs(distance) > 50) { // 50px 이상 드래그하면 슬라이드
            slide(sectionId, distance > 0 ? -1 : 1);
            this.dataset.dragging = 'false';
        }
    });

    // 마우스 클릭을 놓으면 드래그를 멈춤
    section.addEventListener('mouseup', function() {
        this.dataset.dragging = 'false';
    });
}

// 다음 또는 이전 음식 추천으로 슬라이드하는 함수
function slide(sectionId, direction) {
    // 섹션에서 음식 추천 가져오기
    var recommendations = document.querySelectorAll('#' + sectionId + ' .food-recommendation');

    // 현재 활성 추천 숨기기
    recommendations[currentRecommendation[sectionId]].style.display = 'none';

    // 현재 활성 추천 업데이트
    currentRecommendation[sectionId] += direction;
    if (currentRecommendation[sectionId] < 0) {
        currentRecommendation[sectionId] = recommendations.length - 1;
    } else if (currentRecommendation[sectionId] >= recommendations.length) {
        currentRecommendation[sectionId] = 0;
    }

    // 새로운 활성 추천 표시
    recommendations[currentRecommendation[sectionId]].style.display = 'block';
}