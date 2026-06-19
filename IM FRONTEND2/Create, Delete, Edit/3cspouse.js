document.addEventListener("DOMContentLoaded", () => {
const taxpayerId = new URLSearchParams(window.location.search).get("id");

// ======================
// INPUTS
// ======================
const spouseName = document.querySelector("#spouseName");
const spouseTin = document.querySelector("#spouseTin");
const spouseEmployerName = document.querySelector("#spouseEmployerName");
const spouseEmployerTin = document.querySelector("#spouseEmployerTin");
const spouseStatusInputs = document.querySelectorAll("input[name='spouseStatus']");

let spouseId = null;
let isExisting = false;

// ======================
// VIEW SWITCHING (LIST vs FORM)
// ======================
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

// ======================
// LOCK / UNLOCK SYSTEM
// ======================
function setDisabled(state) {
spouseName.disabled = state;
spouseTin.disabled = state;
spouseEmployerName.disabled = state;
spouseEmployerTin.disabled = state;


spouseStatusInputs.forEach(r => r.disabled = state);
}

// start locked
setDisabled(true);

// ======================
// LOAD SPOUSE
// ======================
function loadSpouse() {
fetch(`http://localhost:8080/spouse/by-taxpayer/${taxpayerId}`)
.then(res => {
if (!res.ok) throw new Error("No spouse found");
return res.json();

})
.then(spouse => {
isExisting = true;
spouseId = spouse.id || spouse.spouseId || spouse.spouseTIN;


spouseName.value = spouse.spouseName || "";
spouseTin.value = spouse.spouseTIN || "";
spouseEmployerName.value = spouse.spouseEmpName || "";
spouseEmployerTin.value = spouse.spouseEmpTIN || "";

spouseStatusInputs.forEach(r => {
r.checked = String(spouse.spouseEmpStatus) === r.value;
});

// lock after load
setDisabled(true);
})
.catch(() => {
isExisting = false;
spouseId = null;

setDisabled(false);
});
}

// ======================
// LIST ALL SPOUSES
// ======================
function renderSpouseList() {

if (!recordsListContainer) return;

fetch("http://localhost:8080/spouse")
.then(res => res.json())
.then(data => {

recordsListContainer.innerHTML = "";

if (!data || data.length === 0) {
recordsListContainer.innerHTML =
"<p style='text-align:center;color:#D86B5B;'>No records found.</p>";
return;
}

data.forEach(sp => {

const linkedTaxpayerId =
sp.taxpayer?.tinID ?? sp.taxpayer?.TinID ?? "";

const record = document.createElement("div");
record.className = "record";

record.innerHTML = `

<div class="record-header">
<span class="tin">${sp.spouseTIN || ""}</span>
<span class="name">${sp.spouseName || ""}</span>
<button class="toggle-btn">▼</button>
</div>

<div class="record-details">
<div class="detail-group">

<div class="group-header">
<h3>SPOUSE INFORMATION</h3>
<a class="edit-icon" href="3cspouse.html?id=${linkedTaxpayerId}" title="Edit Spouse"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
</div>

<p><strong>Spouse Name:</strong> ${sp.spouseName || ""}</p>
<p><strong>Spouse TIN:</strong> ${sp.spouseTIN || ""}</p>

<p><strong>Spouse Employment Status:</strong>
${
sp.spouseEmpStatus == 1 ? "Unemployed" :
sp.spouseEmpStatus == 2 ? "Employed Locally" :
sp.spouseEmpStatus == 3 ? "Employed Abroad" :
sp.spouseEmpStatus == 4 ? "Business/Profession" :
""
}
</p>

<p><strong>Spouse Employer's Name:</strong> ${sp.spouseEmpName || ""}</p>
<p><strong>Spouse Employer's TIN:</strong> ${sp.spouseEmpTIN || ""}</p>

</div>
</div>

`;

recordsListContainer.appendChild(record);
});

})
.catch(err => {
console.error("Load spouse list error:", err);
recordsListContainer.innerHTML =
"<p style='text-align:center;color:#D86B5B;'>Backend not reachable.</p>";
});
}

// ======================
// TOGGLE LIST CARDS
// ======================
document.addEventListener("click", (e) => {

const btn = e.target.closest(".toggle-btn");

if (!btn) return;

const record = btn.closest(".record");
const details = record.querySelector(".record-details");

const isOpen = details.classList.toggle("show");

btn.classList.toggle("open", isOpen);

});

// ======================
// INIT
// ======================
if (taxpayerId) {
showForm();
loadSpouse();
} else {
showList();
renderSpouseList();
}

// ======================
// EDIT BUTTON
// ======================
document.querySelector(".edit")?.addEventListener("click", (e) => {
e.preventDefault();
setDisabled(false);

// editing an EXISTING record -> TIN stays locked
if (isExisting && spouseTin) spouseTin.disabled = true;
});

// ======================
// SAVE BUTTON
// ======================
document.querySelector(".save")?.addEventListener("click", async (e) => {
e.preventDefault();

const selectedStatus = document.querySelector(
"input[name='spouseStatus']:checked"
)?.value;

const payload = {
spouseName: spouseName.value,
spouseEmpStatus: Number(selectedStatus || 0),
spouseEmpName: spouseEmployerName.value,
spouseEmpTIN: spouseEmployerTin.value,
taxpayer: { tinID: taxpayerId }
};

try {
let url = "http://localhost:8080/spouse/register";
let method = "POST";

if (isExisting) {
url = `http://localhost:8080/spouse/${spouseId}`;
method = "PATCH";
} else {
payload.spouseTIN = spouseTin.value;
}

const res = await fetch(url, {
method,
headers: { "Content-Type": "application/json" },
body: JSON.stringify(payload)
});

if (!res.ok) throw new Error("Save failed");

const saved = await res.json().catch(() => null);

alert("Saved!");

// record now exists -> lock again, including TIN
isExisting = true;
spouseId = saved?.spouseTIN ?? spouseId ?? spouseTin.value;
setDisabled(true);

} catch (err) {
console.error(err);
alert("Save failed");
}
});

// ======================
// DELETE
// ======================
document.querySelector(".delete")?.addEventListener("click", async (e) => {
e.preventDefault();

if (!spouseId) return alert("Nothing to delete");

await fetch(`http://localhost:8080/spouse/${spouseId}`, {
method: "DELETE"
});

alert("Deleted!");
window.location.href = "3cspouse.html";
});
});