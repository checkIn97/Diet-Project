// // section1 요소를 선택
// var section1 = document.querySelector('#section1');
//
// // 화살표 이미지 요소를 선택
// var arrowL = document.querySelector('#section1 #arrowL');
// var arrowR = document.querySelector('#section1 #arrowR');
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


// 화면 채크 마크 표시
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


// 수량 증가/감소
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