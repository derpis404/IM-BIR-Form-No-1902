console.log("VIEW PAGE JS LOADED");

document.addEventListener("DOMContentLoaded", () => {

const container = document.querySelector("#recordsContainer");


const EMP_TYPE = {
    1: "No Multiple Employment",
    2: "Successive Employments",
    3: "Concurrent Employments"
};



// =========================
// LOAD PREVIOUS EMPLOYERS FIRST
// =========================
const prevListPromise = fetch("http://localhost:8080/prevEmp")
.then(res => res.json())
.catch(err => {
    console.error("PrevEmp load error:", err);
    return [];
});


// =========================
// FETCH TAXPAYERS
// =========================
fetch("http://localhost:8080/taxpayer")
.then(res => res.json())
.then(data => {


container.innerHTML = "";


if (!data || data.length === 0) {

    container.innerHTML =
    "<p style='text-align:center;color:#D86B5B;'>No records found.</p>";

    return;
}



data.forEach(user => {


const id = user.tinID ?? user.TinID;


const record = document.createElement("div");
record.className = "record";



record.innerHTML = `

<div class="record-header">

<span class="tin">${id}</span>
<span class="name">${user.payerName || ""}</span>

<button class="toggle-btn">▼</button>

</div>



<div class="record-details">


<div class="detail-group">

<div class="group-header">
<h3>TAXPAYER INFORMATION</h3>
<a class="edit-icon" href="2ctaxpayer.html?id=${id}" title="Edit Taxpayer"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
</div>


<p><strong>TIN ID:</strong> ${id}</p>

<p><strong>Name:</strong> ${user.payerName || ""}</p>


<p><strong>Payer Type:</strong>
${
user.payerType == 1 ? "Local" :
user.payerType == 2 ? "Resident Alien" :
user.payerType == 3 ? "Special Non-Resident Alien" :
""
}
</p>


<p><strong>Gender:</strong>
${
user.gender == 1 ? "Male" :
user.gender == 2 ? "Female" :
""
}
</p>


<p><strong>Civil Status:</strong>
${
user.civilStatus == 1 ? "Single" :
user.civilStatus == 2 ? "Married" :
user.civilStatus == 3 ? "Widower" :
user.civilStatus == 4 ? "Legally Separated" :
""
}
</p>


<p><strong>Date of Birth:</strong>${user.birthday || ""}</p>

<p><strong>Citizenship:</strong> ${user.citizenship || ""}</p>

<p><strong>Address:</strong> ${user.address || ""}</p>

<p><strong>Contact Number:</strong> ${user.contactNumber || ""}</p>


<p><strong>Employee Type:</strong>
${
user.multiEmpType == 1 ? "No Multiple Employment" :
user.multiEmpType == 2 ? "Successive Employments" :
user.multiEmpType == 3 ? "Concurrent Employments" :
""
}
</p>

</div>




<div class="detail-group current-emp-box-${id}">

<div class="group-header">
<h3>CURRENT EMPLOYER INFORMATION</h3>
<a class="edit-icon" href="1ccurrent.html?id=${id}" title="Edit Current Employer"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
</div>

<p>No Curren Employer record found.</p>

</div>




<div class="detail-group spouse-box-${id}">

<div class="group-header">
<h3>SPOUSE INFORMATION</h3>
<a class="edit-icon" href="3cspouse.html?id=${id}" title="Edit Spouse"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
</div>

<p>No Spouse Records</p>

</div>




<div class="detail-group prev-emp-box-${id}">

<div class="group-header">
<h3>PREVIOUS EMPLOYER INFORMATION</h3>
<a class="edit-icon" href="4cprevious.html?id=${id}" title="Add Previous Employer"><i class="fa fa-plus-square-o" aria-hidden="true"></i></a>
</div>

<p>No Previous Employer</p>

</div>



</div>

`;



container.appendChild(record);





// =========================
// CURRENT EMPLOYER FIX
// =========================

fetch("http://localhost:8080/currentEmp")
.then(res => res.json())
.then(list => {



const emp = list.find(e =>

String(e.currentEmpTIN) === String(id)

);



const box =
document.querySelector(`.current-emp-box-${id}`);



if(!emp){


box.innerHTML = `

<div class="group-header">
<h3>CURRENT EMPLOYER INFORMATION</h3>
<a class="edit-icon" href="1ccurrent.html?id=${id}" title="Edit Current Employer"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
</div>

<p style="color:#D86B5B;">
No employer record found
</p>

`;

return;

}




box.innerHTML = `

<div class="group-header">
<h3>CURRENT EMPLOYER INFORMATION</h3>
<a class="edit-icon" href="1ccurrent.html?id=${id}" title="Edit Current Employer"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
</div>


<p>
<strong>Employer Name:</strong>
${emp.currentEmpName || ""}
</p>


<p>
<strong>Employer TIN:</strong>
${emp.currentEmpTIN || ""}
</p>


<p>
<strong>Address:</strong>
${emp.currentEmpAddress || ""}
</p>


<p>
<strong>Contact:</strong>
${emp.empContactDetails || ""}
</p>


`;



})
.catch(err=>{

console.log("EMP ERROR",err);

});







// =========================
// SPOUSE
// =========================

fetch("http://localhost:8080/spouse")
.then(res => res.json())
.then(list => {

const sp = list.find(s =>
    s.taxpayer &&
    (
        String(s.taxpayer.tinID) === String(id) ||
        String(s.taxpayer.TinID) === String(id)
    )
);

const spBox = document.querySelector(`.spouse-box-${id}`);

if (!spBox) return;

if (!sp) {
    spBox.innerHTML = `
        <div class="detail-group">
            <div class="group-header">
                <h3>SPOUSE INFORMATION</h3>
                <a class="edit-icon" href="3cspouse.html?id=${id}" title="Edit Spouse"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
            </div>
            <p style="color:#D86B5B;">No spouse record found.</p>
        </div>
    `;
    return;
}

// FULL COMPLETE SPOUSE INFO (your required fields)
spBox.innerHTML = `
    <div class="detail-group">
        <div class="group-header">
            <h3>SPOUSE INFORMATION</h3>
            <a class="edit-icon" href="3cspouse.html?id=${id}" title="Edit Spouse"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
        </div>

        <p><strong>Spouse Name:</strong> ${sp.spouseName || ""}</p>
        <p><strong>Spouse TIN:</strong> ${sp.spouseTIN || ""}</p>
        <p><strong>Spouse Employer Name:</strong> ${sp.spouseEmpName || ""}</p>
        <p><strong>Spouse Employer TIN:</strong> ${sp.spouseEmpTIN || ""}</p>
        <p><strong>Employee Type:</strong> ${EMP_TYPE[user.multiEmpType] || ""}</p>
    </div>
`;
})
.catch(err => {
    console.error("SPOUSE ERROR", err);

    const spBox = document.querySelector(`.spouse-box-${id}`);

    if (spBox) {
        spBox.innerHTML = `
            <div class="detail-group">
                <div class="group-header">
                    <h3>SPOUSE INFORMATION</h3>
                    <a class="edit-icon" href="3cspouse.html?id=${id}" title="Edit Spouse"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
                </div>
                <p style="color:red;">Failed to load spouse data</p>
            </div>
        `;
    }
});







// =========================
// PREVIOUS EMPLOYER
// =========================

const prevBox =
document.querySelector(`.prev-emp-box-${id}`);



prevListPromise.then(prevList=>{


const matches =
prevList.filter(p=>{


if(Array.isArray(p.taxpayerIds)){

return p.taxpayerIds
.map(String)
.includes(String(id));

}


if(Array.isArray(p.taxpayers)){


return p.taxpayers.some(t=>

String(t.tinID ?? t.TinID)
===
String(id)

);

}



return false;


});




if(!matches.length){


prevBox.innerHTML = `

<div class="group-header">
<h3>PREVIOUS EMPLOYER INFORMATION</h3>
<a class="edit-icon" href="4cprevious.html?id=${id}" title="Add Previous Employer"><i class="fa fa-plus-square-o" aria-hidden="true"></i></a>
</div>

<p style="color:#D86B5B;">
No previous employer record found
</p>

`;

return;

}




prevBox.innerHTML = `

<div class="group-header">
<h3>PREVIOUS EMPLOYER INFORMATION</h3>
<a class="edit-icon" href="4cprevious.html?id=${id}" title="Add Previous Employer"><i class="fa fa-plus-square-o" aria-hidden="true"></i></a>
</div>


${matches.map((prev, index)=>{

const empTin =
prev.multiEmpTIN ??
prev.MultiEmpTIN ??
prev.multiEmpTin ??
"";

return `

<div class="prev-emp-record">

<div class="prev-emp-label-row">
<a class="edit-icon" href="4cprevious.html?id=${id}&empId=${empTin}" title="Edit Employer ${index + 1}"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
<strong>Employer ${index + 1}</strong>
</div>

<p>
<strong>Employer Name:</strong>
${prev.multiEmpName || ""}
</p>


<p>
<strong>Employer TIN:</strong>
${empTin}
</p>


</div>

`;

}).join("")}


`;



});



});



})
.catch(err=>{

console.error(err);

container.innerHTML =
"Backend not reachable";

});






// =========================
// TOGGLE
// =========================

document.addEventListener("click",(e)=>{


const btn =
e.target.closest(".toggle-btn");


if(!btn)return;



const record =
btn.closest(".record");


const details =
record.querySelector(".record-details");



const isOpen = details.classList.toggle("show");

btn.classList.toggle("open", isOpen);

});



});