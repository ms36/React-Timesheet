// TimeSheet Object
timesheetObject = 
{
    submitted: false,
    timeSheetId: 0,
    user: 
    {
        userId: 0
    },
    'weekDayHours': 
    [
        0, // Monday
        0, // Tuesday
        0, // Wednesday
        0, // Thursday
        0, // Friday
        0, // Saturday
        0  // Sunday
    ],
    weekEndingDate: ""
}
userObject = 
{

};
userId = 0;
       
// Hide/unhide elements
//document.addEventListener('DOMContentLoaded', hideElements('welcomeMessage'));
//document.addEventListener('DOMContentLoaded', hideElements('loginForm'));
document.addEventListener('DOMContentLoaded', hideElements('timesheetTable'));
document.addEventListener('DOMContentLoaded', hideElements('loginOutButton'));
document.addEventListener('DOMContentLoaded', hideElements('saveButton'));
document.addEventListener('DOMContentLoaded', hideElements('submitButton'));

// Clicky click buttons
//document.getElementById('loginOutButton').addEventListener('click', showLoginOut);
document.getElementById('saveButton').addEventListener('click', saveTimesheet);
document.getElementById('submitButton').addEventListener('click', submitTimesheet);

// Hides element(s)
function hideElements(id) 
{
    var el = document.getElementById(id);
    if (el.style.display === "none")
    {
        el.style.display = "block";
    } 
    else
    {
        el.style.display = "none";
    }
} 
/*
// Toggles login/logout
function showLoginOut()
{
    var el = document.getElementById('loginOutButton');
    console.log("showLoginOut");
    console.log(el.innerText);
    if (el.innerText === "Logout")
    {
        //TODO delete tables and welcome message
        var sel1 = document.getElementById('welcomeMessage');
        console.log(sel1);
        for(let i = 1; i < sel1.childElementCount; i++)
        {
            console.log(sel1 + "child: " + i);
            sel1.remove(i);
        }

        var sel2 = document.getElementById('timesheetTable');
        console.log(sel2);
        for(let i = 0; i < sel2.childElementCount; i++)
        {
            console.log(sel2 + "child: " + i);
            sel2.remove(i);
        }
        //hideElements('welcomeMessage');
        //hideElements('timesheetTable');
        //hideElements('loginOutButton');
        //hideElements("loginForm");
        el.innerText = "Login";
        console.log("Logout");
    }
    else if(el.innerText === "Login")
    {
        //hideElements('welcomeMessage');
        hideElements('loginOutButton');
        hideElements("loginForm");
        el.innerText = "Logout";
        console.log("Login");
    }
}*/
// Submit login form 
const myForm = document.getElementById("loginForm");
myForm.addEventListener("submit", (e) => 
{
    console.log(timesheetObject);
    e.preventDefault();
    var formData = new FormData(myForm);
    requestString = '';
    
    // Append the key/value pairs
    for(var pair of formData.entries()) 
    {
       requestString += pair[0]+ '='+ pair[1];               
       requestString += '&';   
    }
    
    requestPost("/login", requestString, appendWelcome);    
});

// Displays "welcome" user's full name
function appendWelcome(user) 
{
    console.log("appendWelcome");
    userObject = user;
    
    //hideElements('loginOutButton');
    hideElements("saveButton") 
    //hideElements("submitButton") 
    let p = document.getElementById('welcomeMessage');
    p.innerText = "WELCOME   " + user.firstName + "  " + user.lastName;
    
    // Get all of this user's timesheets
    requestString = 'userid=' + user.userId;
    requestGet("/timesheet-getall", requestString, appendAllTimeSheets);
} 

// Displays all timesheets
function appendAllTimeSheets(list) 
{
    hideElements("loginForm");
    hideElements('timesheetTable');
    console.log("appendAllTimeSheets");
    lastDate = '';
    var tSheet = timesheetObject;
    for (let timesheet of list) 
    {
        if(timesheet.submitted === true)
        {
            appendTimeSheet(timesheet);
        }
        lastDate = timesheet.weekEndingDate;
        lastSubmit = timesheet.submitted;
        userId = timesheet.user.userId; 
    }
    
    console.log("lastSubmit: " + lastSubmit);
    
    // Create a brand new timesheet
    if(lastSubmit === true)
    {
        console.log("Create new TimeSheet")
        var date = lastDate.split('-');  
        var newDate = addOneWeek(date);
        var requestString = 'userid=' + userId + '&';
        requestString += 'dateEnd=' + newDate;
        requestGet("/timesheet-new", requestString, appendTimeSheet) 
        
        tSheet.weekEndingDate = newDate;
        tSheet.user.userId = userId;
        addTimeSheetInput(tSheet); 
    }
    else // Open an unsubmitted timesheet
    {
        var requestString = 'userid=' + userId + '&';
        requestString += 'dateEnd=' + lastDate;
        requestGet("/timesheet-get", requestString, addTimeSheetInput);  
    }
}

// Displays one timesheet
function appendTimeSheet(timesheet) 
{
    console.log("appendTimeSheet"); 
    tr = document.createElement('tr');
    var totalHours = 0;

    // Appends the hours for each week day to a table
    for (let i = 0; i < timesheet.weekDayHours.length; i++) 
    {
        let td = document.createElement('td');
        td.innerText = timesheet.weekDayHours[i];
        tr.appendChild(td);
        totalHours += timesheet.weekDayHours[i];
    } 
    // Appends the total hours to the table
    let tdTotalHours = document.createElement('td');
    tdTotalHours.innerText = totalHours;
    tr.appendChild(tdTotalHours);

    // Appends the date Ending to the table
    let tdDateEnd = document.createElement('td');
    tdDateEnd.innerText = timesheet.weekEndingDate;
    tr.appendChild(tdDateEnd);

    document.getElementById('timesheetTable').appendChild(tr);
} 

/*
* Fires a post request 
* uri - appends this path to the uri
* request - the request to be sent
* myFunction - calls this function if request is successful
*/
function requestPost(uri, request, myFunction) 
{
    const xhr = new XMLHttpRequest();
    xhr.open("POST", "/timesheet/portal" + uri);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onreadystatechange = function() 
    { 	
        console.log(xhr.readyState);
        if (xhr.readyState === 4 && xhr.status === 200)
        {  
            if(xhr.responseText != "")
            {
                console.log("POST: " + JSON.parse(xhr.responseText));        
                var json = JSON.parse(xhr.responseText);         
                myFunction(json);
            }
        }     	        
    }
    xhr.send(request);
    console.log("SENT " + request + " to " + uri);
};

/*
* Fires a get request 
* uri - appends this path to the uri
* request - appends the request to be sent to the path
* myFunction - calls this function if request is succesfull
*/
function requestGet(uri, request, myFunction) 
{
  var xhr = new XMLHttpRequest();

  xhr.open("GET", "/timesheet/portal" + uri + "?" + request);
  xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
  xhr.onreadystatechange = function() 
  {
    if (xhr.readyState === 4 && xhr.status === 200) 
    {
        if(xhr.responseText != "")
        {
            console.log("GET: " + JSON.parse(xhr.responseText));        
            var json = JSON.parse(xhr.responseText);        
            myFunction(json);
        }
    }
  };

  xhr.send();
  console.log("SENT " + request + " to " + uri);
};

// Add 1 week to a date
// adjusts to new month/year
function addOneWeek(date)
{
    // date[0] = year
    // date[1] = month
    // date[2] = day
    var numOfDaysInMonth = [0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

    // Adjust day
    if( (Number(date[2]) + 7) > numOfDaysInMonth[date[1]])
    {
        date[2] = (Number(date[2]) + 7) - numOfDaysInMonth[date[1]];
        console.log("date:", date[2] );
        // Adjust month
        if((Number(date[1]) + 1) > 12)
        {
            date[0] = Number(date[0]) + 1;

            // If going from December to January
            if(Number(date[1] + 1) > 12)
            {
                date[1] = "01";
            }
            else
            {
                date[1] = Number(date[1]) + 1;
            }
        }
        else
        {
            date[1] = Number(date[1]) + 1;
        }
    }
    else
    {
        date[2] = Number(date[2]) + 7;        
    }

    // Adds a 0 in front if needed to 
    // keep the format yyyy-mm-dd
    if (Number(date[2]) < 10)
    {
        date[2] = "0" + date[2];
    }
    // if (Number(date[1]) < 10)
    // {
    //     date[1] = "0" + date[1];
    // }
    let newDate = date[0] + "-" + date[1] + "-" + date[2]
    console.log("date:", date[2] );
    return newDate;
}
/*
* Creates a new table with dropdown
* menus for the weekDayHours
*/
function addTimeSheetInput(timesheet) 
{
    console.log("addTimeSheetInput");
    timesheetObject = timesheet;
    timesheetObject.user = userObject;
   
    var hoursInADay = [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 
                        12, 13, 14, 15, 16, 17, 18, 19, 20];
   
    var tr = document.createElement('tr');
    var totalHours = 0;

    // Loop for each work day
    for(j = 0; j < 7; j++)
    {
        var newtd = document.createElement('td');
        var selectHTML = "";
        selectHTML="<select onChange='updateTimesheet()' id='cell" + j + "'>";
        
        // Loop for every option for a work day hours
        for(let i = 0; i < hoursInADay.length; i++) 
        {
            // Sets the weekDayHours to selected in the option
            if ( i === timesheet.weekDayHours[j])
            {
                selectHTML += "<option selected value='" + hoursInADay[i] + "'>" + hoursInADay[i] + "</option>";
            }
            else
            {
                selectHTML += "<option value='" + hoursInADay[i] + "'>" + hoursInADay[i] + "</option>";
            }            
        }
        // Appends the options to the table
        selectHTML += "</select>";
        newtd.innerHTML = selectHTML;
        tr.appendChild(newtd);
        totalHours += timesheet.weekDayHours[j];
    }        
        // Appends the total hours to the table
        let tdTotalHours = document.createElement('td');
        tdTotalHours.innerText = totalHours;
        tr.appendChild(tdTotalHours);
        tdTotalHours.setAttribute("id", "totalHours");

        // Appends the date Ending to the table
        let tdDateEnd = document.createElement('td');
        tdDateEnd.innerText = timesheet.weekEndingDate;
        tr.appendChild(tdDateEnd);

        document.getElementById('timesheetTable').appendChild(tr);
}

// Save a timesheet 
function saveTimesheet()
{
    console.log("saveTimesheet");

    // Get the hours for each day
    for( let i = 0; i < 7; i++)
    {
        let el = document.getElementById('cell' + i);
        timesheetObject.weekDayHours[i] = el.options[el.selectedIndex].value;
    }
    var requestString = JSON.stringify(timesheetObject);
    let el = document.getElementById('submitButton');
    if (el.style.display === "none")
    {
        hideElements("submitButton");
    } 
    
    requestPost("/timesheet-save", requestString, alert("Time Sheet has been saved"));     
}

// Submit a timesheet 
function submitTimesheet()
{
    console.log("submitTimesheet");
    var requestString = 'userid=' + timesheetObject.user.userId + '&';
    requestString += 'dateEnd=' + timesheetObject.weekEndingDate;

    let table = document.getElementById('timesheetTable');
    
    requestPost("/timesheet-submit", requestString, alert("Time sheet has been submitted"));
    table.deleteRow(-1);
    requestGet("/timesheet-get", requestString, appendTimeSheet);
    
    hideElements("saveButton");
    hideElements("submitButton");
}

// Updates the total hours for a timesheet
// when the user updates hours for a day
function updateTimesheet()
{
    let sumHours = 0;
    console.log("Called updateTimesheet");
    for( let i = 0; i < 7; i++)
    {
        let el = document.getElementById('cell' + i);
        timesheetObject.weekDayHours[i] = el.options[el.selectedIndex].value;
        sumHours = Number(sumHours) + Number(el.options[el.selectedIndex].value);
    }
    document.getElementById('totalHours').innerText = sumHours;

    let el = document.getElementById('submitButton');
    if (el.style.display === "block")
    {
        hideElements("submitButton");
    } 
}