console.log("PREVIOUS EMP LOADED");

document.addEventListener("DOMContentLoaded", () => {

const id = new URLSearchParams(window.location.search).get("id");
const empId = new URLSearchParams(window.location.search).get("empId");

const prevEmployerName = document.querySelector(".prevEmployerName");
const prevEmployerTin = document.querySelector(".prevEmployerTin");


// =========================
// VIEW SWITCHING (LIST vs FORM)
// =========================
const formView = document.querySelector("#formView");
const listView = document.querySelector("#listView");
const recordsListContainer = document.querySelector("#recordsListContainer");

function showForm() {
if (formView) formView.style.display = "";
if (listView) listView.style.display = "none";
}

function showList() {
if (formView) formView.style.display = "none";
if (listView) listView.style.display = "";
}


// =========================
// LOCK FUNCTION
// =========================
let isExisting = false;

function setDisabled(state) {
if (prevEmployerName) prevEmployerName.disabled = state;
if (prevEmployerTin) prevEmployerTin.disabled = state;
}


// =========================
// LOAD ONE SPECIFIC PREVIOUS EMP (BY ITS OWN TIN)
// =========================

async function loadPreviousEmployer(){

try{

const res = await fetch(
`http://localhost:8080/prevEmp/${empId}`
);

if(!res.ok) throw new Error("Previous employer not found");

const existing = await res.json();

console.log("LOADED PREV EMP:", existing);



// =========================
// FILL DATA
// =========================

prevEmployerName.value =
existing.multiEmpName || "";


prevEmployerTin.value =
existing.MultiEmpTIN || "";



// existing record -> lock like taxpayer
isExisting = true;
setDisabled(true);


}

catch(err){

console.error(
"LOAD PREV EMP ERROR:",
err
);

isExisting = false;
setDisabled(false);

}

}


// =========================
// LIST ALL PREVIOUS/CONCURRENT EMPLOYERS
// =========================

function renderPrevEmpList(){

if (!recordsListContainer) return;

fetch("http://localhost:8080/prevEmp")
.then(res => res.json())
.then(data => {

recordsListContainer.innerHTML = "";

if (!data || data.length === 0) {
recordsListContainer.innerHTML =
"<p style='text-align:center;color:#D86B5B;'>No records found.</p>";
return;
}

data.forEach(prev => {

const empTin =
prev.multiEmpTIN ??
prev.MultiEmpTIN ??
prev.multiEmpTin ??
"";

const taxpayerIds = Array.isArray(prev.taxpayerIds) ? prev.taxpayerIds : [];
const firstTaxpayerId = taxpayerIds[0] ?? "";

const record = document.createElement("div");
record.className = "record";

record.innerHTML = `

<div class="record-header">
<span class="tin">${empTin}</span>
<span class="name">${prev.multiEmpName || ""}</span>
<button class="toggle-btn">▼</button>
</div>

<div class="record-details">
<div class="detail-group">

<div class="group-header">
<h3>PREVIOUS EMPLOYER INFORMATION</h3>
<a class="edit-icon" href="4cprevious.html?id=${firstTaxpayerId}&empId=${empTin}" title="Edit Previous Employer"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
</div>

<p><strong>Employer Name:</strong> ${prev.multiEmpName || ""}</p>
<p><strong>Employer TIN:</strong> ${empTin}</p>
<p><strong>Linked Taxpayer ID(s):</strong> ${taxpayerIds.join(", ") || "None"}</p>

</div>
</div>

`;

recordsListContainer.appendChild(record);
});

})
.catch(err => {
console.error("Load previous employer list error:", err);
recordsListContainer.innerHTML =
"<p style='text-align:center;color:#D86B5B;'>Backend not reachable.</p>";
});
}

// =========================
// TOGGLE LIST CARDS
// =========================

document.addEventListener("click", (e) => {

const btn = e.target.closest(".toggle-btn");

if (!btn) return;

const record = btn.closest(".record");
const details = record.querySelector(".record-details");

const isOpen = details.classList.toggle("show");

btn.classList.toggle("open", isOpen);

});


// =========================
// INIT
// =========================
// empId in the URL => edit that one specific record
// id only (no empId) => start a brand-new entry for that taxpayer
// neither => show the list of all previous/concurrent employers

if(empId){

showForm();
loadPreviousEmployer();

} else if(id){

showForm();
isExisting = false;
setDisabled(false);

} else {

showList();
renderPrevEmpList();

}


// =========================
// EDIT
// =========================

document.querySelector(".edit")
?.addEventListener("click",(e)=>{

e.preventDefault();

setDisabled(false);

// editing an EXISTING record -> TIN stays locked
if (isExisting && prevEmployerTin) prevEmployerTin.disabled = true;

});




// =========================
// SAVE
// =========================

document.querySelector(".save")
?.addEventListener("click", async(e)=>{

e.preventDefault();


const payload = {

MultiEmpTIN:
prevEmployerTin.value,

multiEmpName:
prevEmployerName.value,

taxpayerIds:
id ? [String(id)] : []

};



const res = await fetch(
"http://localhost:8080/register/prevEmp",
{
method:"POST",
headers:{
"Content-Type":"application/json"
},
body:JSON.stringify(payload)
}
);



if(res.ok){

alert("Saved!");

isExisting = true;
setDisabled(true);

}else{

alert("Save failed");

}



});




// =========================
// DELETE
// =========================

document.querySelector(".delete")
?.addEventListener("click", async(e)=>{

e.preventDefault();

if(!prevEmployerTin.value){

alert("No record");

return;

}


const res = await fetch(
`http://localhost:8080/prevEmp/${prevEmployerTin.value}`,
{
method:"DELETE"
}
);



if(res.ok){

alert("Deleted!");

window.location.href="4cprevious.html";

}

});


});