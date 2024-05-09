var carbohydrateChart = document.getElementById('carbohydrateChart').querySelector('.chart-bar');
var proteinChart = document.getElementById('proteinChart').querySelector('.chart-bar');
var fatChart = document.getElementById('fatChart').querySelector('.chart-bar');
var kcalChart = document.getElementById('kcalChart').querySelector('.chart-bar');

// 각 차트의 값을 0에서 100 사이의 값으로 설정합니다.
var carbohydrateValue = 50;
var proteinValue = 100;
var fatValue = 30;
var kcalValue = 70;

// 각 차트의 현재 값을 나타내는 바의 너비를 변경하여 차트의 값을 표시합니다.
carbohydrateChart.style.width = carbohydrateValue + '%';
proteinChart.style.width = proteinValue + '%';
fatChart.style.width = fatValue + '%';
kcalChart.style.width = kcalValue + '%';

// 차트의 애니메이션을 시작합니다.
window.onload = function() {
    animateChart("carbohydrateChart", 200, 400);
    animateChart("proteinChart", 700, 700);
    animateChart("fatChart", 30, 100);
    animateChart("kcalChart", 1430, 2100);
}

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