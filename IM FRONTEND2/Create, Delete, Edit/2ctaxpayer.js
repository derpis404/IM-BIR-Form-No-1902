const id = new URLSearchParams(window.location.search).get("id");

// =======================
// INPUTS
// =======================
const payerName = document.querySelector("#payerName");
const tinID = document.querySelector("#tinID");
const birthday = document.querySelector("#birthday");
const motherName = document.querySelector("#motherName");
const fatherName = document.querySelector("#fatherName");
const citizenship = document.querySelector("#citizenship");
const address = document.querySelector("#address");
const idType = document.querySelector("#idType");
const idNumber = document.querySelector("#idNumber");
const idEffectiveDate = document.querySelector("#idEffectiveDate");
const idExpiryDate = document.querySelector("#idExpiryDate");
const birthPlace = document.querySelector("#birthPlace");
const contactNumber = document.querySelector("#contactNumber");
const hiredDate = document.querySelector("#hiredDate");

// =======================
// LOCK FUNCTION
// =======================
function setDisabled(state) {
[
payerName,
tinID,
birthday,
motherName,
fatherName,
citizenship,
address,
idType,
idNumber,
idEffectiveDate,
idExpiryDate,
birthPlace,
contactNumber,
hiredDate
].forEach(el => {
if (el) el.disabled = state;
});

document.querySelectorAll('input[name="taxtype"]').forEach(r => r.disabled = state);
document.querySelectorAll('input[name="gender"]').forEach(r => r.disabled = state);
document.querySelectorAll('input[name="civilStatus"]').forEach(r => r.disabled = state);
document.querySelectorAll('input[name="multiEmpType"]').forEach(r => r.disabled = state);
}

// =======================
// LOAD FUNCTION
// =======================
function loadTaxpayer() {
fetch(`http://localhost:8080/taxpayer/${id}`)
.then(res => res.json())
.then(data => {

  payerName.value = data.payerName || "";

  // ✅ FIXED CASE HANDLING
  tinID.value = data.tinID || data.TinID || "";

  birthday.value = data.birthday || "";
  motherName.value = data.motherName || "";
  fatherName.value = data.fatherName || "";
  citizenship.value = data.citizenship || "";
  address.value = data.address || "";
  idType.value = data.idType || "";
  idNumber.value = data.idNumber || "";
  idEffectiveDate.value = data.idEffectiveDate || "";
  idExpiryDate.value = data.idExpiryDate || "";
  birthPlace.value = data.birthPlace || "";
  contactNumber.value = data.contactNumber || "";

  hiredDate.value = data.hiredDate
    ? data.hiredDate.split("T")[0]
    : "";

  document.querySelectorAll('input[name="taxtype"]').forEach(r => {
    r.checked = String(r.value) === String(data.payerType);
  });

  document.querySelectorAll('input[name="gender"]').forEach(r => {
    r.checked = String(r.value) === String(data.gender);
  });

  document.querySelectorAll('input[name="civilStatus"]').forEach(r => {
    r.checked = String(r.value) === String(data.civilStatus);
  });

  document.querySelectorAll('input[name="multiEmpType"]').forEach(r => {
    r.checked = String(data.multiEmpType) === r.value;
  });

  setDisabled(true);
})
.catch(err => console.log("Load error:", err));
}

// =======================
// VIEW/CREATE MODE
// =======================
document.addEventListener("DOMContentLoaded", () => {

if (id) {
loadTaxpayer();
} else {
setDisabled(false);
}

});

// =======================
// EDIT BUTTON
// =======================
document.querySelector(".edit").addEventListener("click", (e) => {
e.preventDefault();
setDisabled(false);
tinID.disabled = true;
});

// =======================
// SAVE BUTTON
// =======================
document.querySelector(".save").addEventListener("click", async (e) => {
e.preventDefault();

const updated = {
payerName: payerName.value,


tinID: tinID.value,

birthday: birthday.value,
birthPlace: birthPlace.value,
contactNumber: contactNumber.value,
hiredDate: hiredDate.value,

motherName: motherName.value,
fatherName: fatherName.value,
citizenship: citizenship.value,
address: address.value,

idType: idType.value,
idNumber: idNumber.value,
idEffectiveDate: idEffectiveDate.value,
idExpiryDate: idExpiryDate.value,

payerType: Number(document.querySelector('input[name="taxtype"]:checked')?.value),
gender: Number(document.querySelector('input[name="gender"]:checked')?.value),
civilStatus: Number(document.querySelector('input[name="civilStatus"]:checked')?.value),
multiEmpType: Number(document.querySelector('input[name="multiEmpType"]:checked')?.value || 0)
};

const isUpdate = !!id;

const url = isUpdate
  ? `http://localhost:8080/taxpayer/${id}`
  : `http://localhost:8080/register/taxpayer`;

const method = isUpdate ? "PUT" : "POST";

const res = await fetch(url, {
method,
headers: { "Content-Type": "application/json" },
body: JSON.stringify(updated)
});

if (!res.ok) {
const msg = await res.text();
console.error("SAVE FAILED:", msg);
alert("Save failed. Check console.");
return;
}

alert("Saved!");
setDisabled(true);

if (!isUpdate) {
window.location.href = "0view.html";
}
});

// =======================
// DELETE BUTTON
// =======================
document.querySelector(".delete").addEventListener("click", async (e) => {
e.preventDefault();

if (!id) return alert("No record selected");

await fetch(`http://localhost:8080/taxpayer/${id}`, {
method: "DELETE"
});

alert("Deleted!");
window.location.href = "2ctaxpayer.html";
});