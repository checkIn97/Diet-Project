$(document).ready(function() {
	$.ajax({
	    type: 'GET',
	    url: '/load_userVo',
	    dataType: 'json',
	    success: function (data) {
	        userVo = data;
	        var carbohydrateChart = document.getElementById('carbohydrateChart').querySelector('.chart-bar');
			var proteinChart = document.getElementById('proteinChart').querySelector('.chart-bar');
			var fatChart = document.getElementById('fatChart').querySelector('.chart-bar');
			var kcalChart = document.getElementById('kcalChart').querySelector('.chart-bar');
			
			// 각 차트의 값을 0에서 100 사이의 값으로 설정합니다.
			var carbohydrateValue = userVo.carbToday > userVo.properCarb ? 100 : userVo.carbToday*100/userVo.properCarb;
			var proteinValue = userVo.prtToday > userVo.properPrt ? 100 : userVo.prtToday*100/userVo.properPrt;
			var fatValue = userVo.fatToday > userVo.properFat ? 100 : userVo.fatToday*100/userVo.properFat;
			var kcalValue = userVo.kcalToday > userVo.EER ? 100 : userVo.kcalToday*100/userVo.EER;
			
			// 각 차트의 현재 값을 나타내는 바의 너비를 변경하여 차트의 값을 표시합니다.
			carbohydrateChart.style.width = carbohydrateValue + '%';
			proteinChart.style.width = proteinValue + '%';
			fatChart.style.width = fatValue + '%';
			kcalChart.style.width = kcalValue + '%';
			
			// 차트의 애니메이션을 시작합니다.
		    animateChart("carbohydrateChart", userVo.carbToday > userVo.properCarb ? userVo.properCarb : userVo.carbToday, userVo.properCarb);
		    animateChart("proteinChart", userVo.prtToday > userVo.properPrt ? userVo.properPrt : userVo.prtToday, userVo.properPrt);
		    animateChart("fatChart", userVo.fatToday > userVo.properFat ? userVo.properFat : userVo.fatToday, userVo.properFat);
		    animateChart("kcalChart", userVo.kcalToday > userVo.EER ? userVo.EER : userVo.kcalToday, userVo.EER);
	    },
	    error: function () {
	
	    }
	});	
});


function animateChart(chartId, currentValue, maxValue) {
    var chart = document.getElementById(chartId);
    var chartBar = chart.querySelector(".chart-bar");
    var percentage = (currentValue / maxValue) * 100;

    var currentWidth = 0;
    var interval = setInterval(function() {
        if (currentWidth >= percentage) {
            clearInterval(interval);
        } else {
            currentWidth++;
            chartBar.style.width = currentWidth + "%";
        }
    }, 20); // 20ms 간격으로 너비를 증가시킵니다.
}