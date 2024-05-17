// 테이블 요소를 가져옵니다
var activityTable = document.getElementById('mytable');

function initAutocomplete() {
    $("input[name='activityType']").autocomplete({
        source: function (request, response) {
            // AJAX 요청을 사용하여 서버에서 자동완성 데이터를 가져옵니다.
            $.ajax({
                url: "/activities/search", // 수정된 URL
                type: "GET",
                data: {
                    term: request.term
                },
                success: function (data) {
                    // 가져온 데이터를 자동완성 목록에 표시합니다.
                    response(data);
                }
            });
        },
        minLength: 1,
        // autofocus: true,

        select: function (event, ui) {
            // 사용자가 자동완성 목록에서 항목을 선택하면 입력 필드의 값을 해당 항목의 값으로 설정합니다.
            $(this).val(ui.item.value);
        },
        change: function (event, ui) {
            var input = $(this);
            if (!ui.item) {
                $.ajax({
                    url: "/activities/validate", // 유효성 검사를 위한 서버의 URL
                    type: "GET",
                    data: {
                        term: input.val()
                    },
                    success: function (data) {
                        if (!data) { // 서버가 false를 반환하면 입력한 값이 유효하지 않다는 것입니다.
                            event.preventDefault();
                            input.val("");
                            alert("사용할 수 없는 운동 종류입니다.");
                        }
                    }
                });
            }
        }
    });
}

// 값이 비어 있는 경우
document.querySelector('form').addEventListener('submit', function (event) {
    var inputs = document.querySelectorAll('input[type="number"], input[type="text"]');
    for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].value === '') {
            // 이벤트를 취소
            event.preventDefault();
            alert('모든 값을 채워주세요');
            $('input[name="activityType"]').focus(); // 수정된 입력 필드 이름
            return;
        }
    }
});
