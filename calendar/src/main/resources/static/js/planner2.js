// 현재 날짜
let current_year;
let current_month;
let current_date;
let current_day;

document.addEventListener('DOMContentLoaded', function(){
    console.log('addEventListener()');

    // 오늘 날짜
    let today = new Date();
    let today_year = today.getFullYear();
    let today_month = today.getMonth();
    let today_date = today.getDate();
    let today_day = today.getDay();

    // 현재 날짜 세팅

    setCurrentCalendar(today_year, today_month, today_date, today_day);

    // 현재 <select> UI 렌더링
    setCurrentYearAndMonthSelectUI();

    // 현재 <tr> UI 렌더링
    addCalendarTr();

    // 이벤트 핸들러
    initEvents();

});

function setCurrentCalendar(year, month, date, day) {
    console.log('setCurrentCalendar()');

    current_year = year;
    current_month = month;
    current_date = date;
    current_day = day;

}

function setCurrentYearAndMonthSelectUI() {
    console.log('setCurrentYearAndMonthSelectUI()');

    document.querySelector('#select_wrap select[name"p_year"]').value = current_year;
    document.querySelector('#select_wrap select[name"p_month"]').value = current_month + 1;

}

function addCalendarTr() {
    console.log('addCalendarTr()');

    let thisCalendarStart = new Date(current_year, current_month, 1);
    let thisCalendarStartDate = thisCalendarStart.getDate();
    let thisCalendarStartDay = thisCalendarStartDay.getDay();

    let thisCalendarEnd = new Date(current_year, current_month + 1, 0);
    let thisCalendarEndDate = thisCalendarEnd.getDate();

    // 달력 구성 날짜 데이터
    let dates = Array();
    let dateCnt = 1;
    for (let i = 0; i < 42; i++) {
        if (i < thisCalendarStartDay || dateCnt > thisCalendarEndDate) {
            dates[i] = 0;
        } else {
            dates[i] = dateCnt;
            dateCnt++;
        }
    }

    // UI(<tr>)
    let tableBody = document.querySelector('#table_calender tbody');

    let dateIndex = 0;
    for (let i = 0; i < 6; i++) {

        if (i >= 5 && dates[dateIndex] === 0) break;

        let tr = document.createElement('tr');

        for(let j = 0; j < 7; j++) {
            let td = document.createElement('td');

            if (date[dateIndex] !== 0) {
                //날짜 UI
                let dateDiv = document.createElement('div');
                dateDiv.className = 'date';
                dateDiv.textContent = dates[dateIndex];
                td.appendChild(dateDiv);
                
            }
        }
    }
    
}

