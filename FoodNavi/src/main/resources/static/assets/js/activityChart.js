Chart.defaults.color = '#eee'; // 전역으로 글씨 색상을 흰색으로 변경

var ctx = document.getElementById('myChart').getContext('2d');

var myChart = new Chart(ctx, {
    type: 'bar',
    data: {
        labels: ['일', '월', '화', '수', '목', '금', '토'],
        datasets: [{
            label: '운동시간',
            data: [12, 19, 3, 5, 2, 3, 7],
            backgroundColor: 'rgba(75, 192, 192, 0.2)',
            borderColor: 'rgba(75, 192, 192, 1)',
            borderWidth: 1
        }]
    },
    options: {
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    fontColor: 'white' // y축 레이블의 글씨 색상을 흰색으로 변경
                }
            },
            x: {
                ticks: {
                    fontColor: 'white' // x축 레이블의 글씨 색상을 흰색으로 변경
                }
            }
        },
        legend: {
            labels: {
                fontColor: 'white' // 범례의 글씨 색상을 흰색으로 변경
            }
        }
    }
});