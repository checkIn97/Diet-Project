// 모든 섹션 요소를 선택
var sections = document.querySelectorAll('.section');

sections.forEach(function(section) {
    // 각 섹션 내의 화살표 이미지 요소를 선택
    var arrowL = section.querySelector('.arrowL');
    var arrowR = section.querySelector('.arrowR');

    // 섹션 요소에 마우스가 hover 되었을 때
    section.addEventListener('mouseover', function() {
        // 화살표 이미지를 표시
        arrowL.style.display = 'block';
        arrowR.style.display = 'block';
    });

    // 섹션 요소에서 마우스가 떠났을 때
    section.addEventListener('mouseout', function() {
        // 화살표 이미지를 숨김
        arrowL.style.display = 'none';
        arrowR.style.display = 'none';
    });
});

/*화면 채크 마크 표시*/
var sections = document.getElementsByClassName('section');
for (var i = 0; i < sections.length; i++) {
    sections[i].addEventListener('click', function(event) {
        // 'quantity' 클래스를 가진 요소를 포함하는 'section' 요소를 클릭한 경우 이벤트 핸들러를 실행하지 않음
        var quantity = this.querySelector('.quantity');
        if (quantity && quantity.contains(event.target)) {
            return;
        }

        var arrow = this.querySelector('.arrow');
        if (arrow && arrow.contains(event.target)) {
            return;
        }

        var foodDetailGo = this.querySelector('.foodDetailGo');
        if (foodDetailGo && foodDetailGo.contains(event.target)) {
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

// 각 섹션에 대한 참조를 가져옵니다.
var section1 = document.getElementById('section1');
var section1_1 = document.getElementById('section1_1');

// 각 화살표에 대한 참조를 가져옵니다.
var arrowL1 = document.getElementById('arrowL1');
var arrowR1 = document.getElementById('arrowR1');
var arrowL1_1 = document.getElementById('arrowL1_1');
var arrowR1_1 = document.getElementById('arrowR1_1');

// 화살표에 클릭 이벤트 리스너를 추가합니다.
arrowR1.addEventListener('click', function() {
    section1.style.display = 'none';
    section1_1.style.display = 'block';
});

arrowL1_1.addEventListener('click', function() {
    section1_1.style.display = 'none';
    section1.style.display = 'block';
});

