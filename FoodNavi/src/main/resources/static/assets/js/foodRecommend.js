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
function click_section(n) {
	let s = null;
	let s3 = document.querySelector("#section3");
	if (n == 1) {
		s = document.querySelector("#section1");
	} else if (n == 2) {
		s = document.querySelector("#section2");
	} else if (n ==3) {
		s = document.querySelector("#section3");
	}
	let quantity = s.querySelector('.quantity');
    if (quantity && quantity.contains(event.target)) {
        return;
    }

    let arrow = s.querySelector('.arrow');
    if (arrow && arrow.contains(event.target)) {
        return;
    }

    let foodDetailGo = s.querySelector('.foodDetailGo');
    if (foodDetailGo && foodDetailGo.contains(event.target)) {
        return;
    }
	
    let checkmark = s.querySelector('.checkmark');
    if (s.dataset.clicked === 'true') {
        s.style.backgroundColor = ''; // 배경색을 원래대로 복원
        if (checkmark) {
            checkmark.style.display = 'none'; // 이미지를 숨김
        }
        s.dataset.clicked = 'false';
        if (n == 1) {
			$("#fseq1").val(0);
		} else if (n == 2) {
			$("#fseq2").val(0);
		} else if (n == 3) {
			$("#fseq3").val(0);
		}
    } else {
        s.style.backgroundColor = 'rgba(255, 255, 255, 0.5)'; // 흰색으로 덮어씌움
        if (checkmark) {
            checkmark.style.display = 'block'; // 이미지를 표시
        }
        s.dataset.clicked = 'true';
        let fseq1Original = $("#fseq1Original").val();
        let fseq2Original = $("#fseq2Original").val();
        let fseq3Original = $("#fseq3Original").val();
        if (n == 1) {
			$("#fseq1").val(fseq1Original);
		} else if (n == 2) {
			$("#fseq2").val(fseq2Original);
		} else if (n == 3) {
			$("#fseq3").val(fseq3Original);
		}
    }
}



/*수량 증가/감소*/
var quantities = document.getElementsByClassName('quantity');
function change_amount(i, direction) {
	let decreaseButton = quantities[i-1].getElementsByClassName('decrease')[0];
	let increaseButton = quantities[i-1].getElementsByClassName('increase')[0];
	let input = quantities[i-1].getElementsByClassName('quantity-input')[0];
	let currentValue = Number(input.value); 
	if (direction == 0) {
		if (currentValue > 1) {
	        input.value = currentValue - 1;
	    }
	} else {
		if (currentValue < 99) {
	        input.value = currentValue + 1;
	    }
	}
}


/*슬라이드 기능*/

// 각 섹션에 대한 참조를 가져옵니다.
var section1 = document.getElementById('section1');
var section2 = document.getElementById('section2');
var section3 = document.getElementById('section3');

// 각 화살표에 대한 참조를 가져옵니다.
var arrowL1 = document.getElementById('arrowL1');
var arrowR1 = document.getElementById('arrowR1');
var arrowL2 = document.getElementById('arrowL2');
var arrowR2 = document.getElementById('arrowR2');
var arrowL3 = document.getElementById('arrowL3');
var arrowR3 = document.getElementById('arrowR3');


// 화살표에 클릭 이벤트 리스너를 추가합니다.
arrowL1.addEventListener('click', function() {
	$("#arrow_action").val(1);
	reloadSection();
});

arrowR1.addEventListener('click', function() {
	$("#arrow_action").val(2);
	reloadSection();
});

arrowL2.addEventListener('click', function() {
	$("#arrow_action").val(3);
	reloadSection();
});

arrowR2.addEventListener('click', function() {
	$("#arrow_action").val(4);
	reloadSection();
});

arrowL3.addEventListener('click', function() {
	$("#arrow_action").val(5);
	reloadSection();
});

arrowR3.addEventListener('click', function() {
	$("#arrow_action").val(6);
	reloadSection();
});

function reloadSection() {
	$.ajax({
		type: 'POST',
    	url: '/recommend_section_reload',
    	dataType: 'json',
    	data: $("#arrow_action"),
    	contentType: 'application/x-www-form-urlencoded; charset=utf-8',
    	success: function (data) {
	        var result = data.result;
	        if (result == 'success') {
				var food_name = data.food_name;
		        var fseq = data.fseq;
	        	var section_num = data.section_num;
	        	var kcal = data.kcal;
	        	var carb = data.carb;
	        	var prt = data.prt;
	        	var fat = data.fat;
	        	var fi_name = data.fi_name;
	        	var fi_amount = data.fi_amount;
	        	var starScore = data.starScore;
	        	var scoreView = data.scoreView;
	        	var html = "";
	        	html += "<table class=\"food-info\">";;
				html += "<tr><span class=\"food-name\">"+food_name+"</span>";
				html += "<a href=\"food_detail?fseq="+fseq+"&type=r"+section_num+"\">";
				html += "<img src=\"/assets/images/reading-glasses.png\" class=\"foodDetailGo\" title=\"상세보기\" alt=\"상세보기\">";
				html == "</a></tr>";
				html += "<tr><td style=\"width:40%\">칼로리</td>";
				html += "<td><span class=\"calories\">"+kcal+"</span>kcal</td></tr>";
				html += "<tr><td>탄수화물</td>";
				html += "<td><span class=\"carbs\">"+carb+"</span>g</td></tr>";
				html += "<tr><td>단백질</td>";
				html += "<td><span class=\"protein\">"+prt+"</span>g</td></tr>";
				html += "<tr><td>지방</td>";
				html += "<td><span class=\"fat\">"+fat+"</span>g</td></tr>";
				html += "<tr><td>재료</td><td>";
				
				if (fi_name.length > 0) {
					for (let i = 0 ; i < fi_name.length ; i++) {
						html += "<span>"+fi_name[i]+" "+fi_amount[i]+"g</span><br>";
					}
				}
				
				html += "</td></tr>";
				html += "<tr><td>추천점수</td>";
				html += "<td><span class=\"satisfaction\" title="+scoreView+"%"+" alt="+scoreView+">";
				if (starScore == 0) {
					html += "<img class=\"star\" src=\"/assets/images/star-empty.png\">";	
				} else if (starScore == 1) {
					html += "<img class=\"star\" src=\"/assets/images/star-half.png\">";
				} else if (starScore >= 2) {
					html += "<img class=\"star\" src=\"/assets/images/star-full.png\">";
				}
				html += "&nbsp";
				
				if (starScore == 2) {
					html += "<img class=\"star\" src=\"/assets/images/star-empty.png\">";	
				} else if (starScore == 3) {
					html += "<img class=\"star\" src=\"/assets/images/star-half.png\">";
				} else if (starScore >= 4) {
					html += "<img class=\"star\" src=\"/assets/images/star-full.png\">";
				}
				html += "&nbsp";
				
				if (starScore == 4) {
					html += "<img class=\"star\" src=\"/assets/images/star-empty.png\">";	
				} else if (starScore == 5) {
					html += "<img class=\"star\" src=\"/assets/images/star-half.png\">";
				} else if (starScore >= 6) {
					html += "<img class=\"star\" src=\"/assets/images/star-full.png\">";
				}
				html += "&nbsp";
				
				if (starScore == 6) {
					html += "<img class=\"star\" src=\"/assets/images/star-empty.png\">";	
				} else if (starScore == 7) {
					html += "<img class=\"star\" src=\"/assets/images/star-half.png\">";
				} else if (starScore >= 8) {
					html += "<img class=\"star\" src=\"/assets/images/star-full.png\">";
				}
				html += "&nbsp";
				
				if (starScore == 8) {
					html += "<img class=\"star\" src=\"/assets/images/star-empty.png\">";	
				} else if (starScore == 9) {
					html += "<img class=\"star\" src=\"/assets/images/star-half.png\">";
				} else if (starScore >= 10) {
					html += "<img class=\"star\" src=\"/assets/images/star-full.png\">";
				}
				
				html += "</span></td></tr>";
				html += "<tr><td>수량</td>";
				html += "<td><span class=\"quantity\">";
				html += "<button class=\"decrease\" onclick=\"change_amount("+section_num+", 0)\"><img class=\"pmIcon\" src=\"/assets/images/minus.png\"></button>";
				html += "<input class=\"quantity-input\" min=\"1\" max=\"99\" type=\"number\" value=\"1\" readonly>";
				html += "<button class=\"increase\" onclick=\"change_amount("+section_num+", 1)\"><img class=\"pmIcon\" src=\"/assets/images/plus.png\"></button>";
				html += "</span></td></tr></table>";
				if (section_num == 1) {
					$("#food_info1").html(html);	
				} else if (section_num == 2) {
					$("#food_info2").html(html);
				} else {
					$("#food_info3").html(html);
				}
				
			}
    	},
    	error: function () {
			alert("error");
    	}
	});
}


