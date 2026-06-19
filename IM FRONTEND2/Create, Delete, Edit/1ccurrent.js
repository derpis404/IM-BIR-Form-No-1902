console.log("CURRENT EMPLOYEE JS LOADED");

// =======================
// URL PARAM (PRIMARY KEY)
// =======================
const taxpayerId = new URLSearchParams(window.location.search).get("id");

// =======================
// VIEW SWITCHING (LIST vs FORM)
// =======================
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

// =======================
// INPUTS
// =======================
const empName = document.querySelector("#empName");
const empTin = document.querySelector("#empTin");
const empAddress = document.querySelector("#empAddress");
const empContact = document.querySelector("#empContact");

// =======================
// LOCK FUNCTION
// =======================
function setDisabled(state) {
    [empName, empTin, empAddress, empContact].forEach(el => {
        if (el) el.disabled = state;
    });
}

// =======================
// LOAD EMPLOYER (FIXED)
// =======================
function loadCurrentEmp() {
    if (!taxpayerId) return;

    fetch(`http://localhost:8080/currentEmp/${taxpayerId}`)
        .then(res => {
            if (!res.ok) throw new Error("No record");
            return res.json();
        })
        .then(data => {
            console.log("BACKEND RESPONSE:", data);

            // IMPORTANT: trust backend field ONLY
            empTin.value = data.currentEmpTIN ?? "";
            empName.value = data.currentEmpName ?? "";
            empAddress.value = data.currentEmpAddress ?? "";
            empContact.value = data.empContactDetails ?? "";

            setDisabled(true);
        })
        .catch(err => console.log("Load error:", err));
}

// =======================
// LIST ALL CURRENT EMPLOYERS
// =======================
function renderCurrentEmpList() {

    if (!recordsListContainer) return;

    fetch("http://localhost:8080/currentEmp")
        .then(res => res.json())
        .then(data => {

            recordsListContainer.innerHTML = "";

            if (!data || data.length === 0) {
                recordsListContainer.innerHTML =
                    "<p style='text-align:center;color:#D86B5B;'>No records found.</p>";
                return;
            }

            data.forEach(emp => {

                const record = document.createElement("div");
                record.className = "record";

                record.innerHTML = `

<div class="record-header">
<span class="tin">${emp.currentEmpTIN || ""}</span>
<span class="name">${emp.currentEmpName || ""}</span>
<button class="toggle-btn">▼</button>
</div>

<div class="record-details">
<div class="detail-group">

<div class="group-header">
<h3>CURRENT EMPLOYER INFORMATION</h3>
<a class="edit-icon" href="1ccurrent.html?id=${emp.currentEmpTIN}" title="Edit Current Employer"><i class="fa fa-pencil-square-o" aria-hidden="true"></i></a>
</div>

<p><strong>Employer Name:</strong> ${emp.currentEmpName || ""}</p>
<p><strong>Employer TIN:</strong> ${emp.currentEmpTIN || ""}</p>
<p><strong>Address:</strong> ${emp.currentEmpAddress || ""}</p>
<p><strong>Contact:</strong> ${emp.empContactDetails || ""}</p>

</div>
</div>

                `;

                recordsListContainer.appendChild(record);
            });

        })
        .catch(err => {
            console.error("Load current employer list error:", err);
            recordsListContainer.innerHTML =
                "<p style='text-align:center;color:#D86B5B;'>Backend not reachable.</p>";
        });
}

// =======================
// INIT
// =======================
document.addEventListener("DOMContentLoaded", () => {
    if (taxpayerId) {
        showForm();
        loadCurrentEmp();
    } else {
        showList();
        renderCurrentEmpList();
    }
});

// =======================
// TOGGLE LIST CARDS
// =======================
document.addEventListener("click", (e) => {

    const btn = e.target.closest(".toggle-btn");

    if (!btn) return;

    const record = btn.closest(".record");
    const details = record.querySelector(".record-details");

    const isOpen = details.classList.toggle("show");

    btn.classList.toggle("open", isOpen);

});

// =======================
// EDIT BUTTON
// =======================
document.querySelector("#editBtn").addEventListener("click", (e) => {
    e.preventDefault();

    setDisabled(false);

    // force TIN locked
    if (empTin) empTin.disabled = true;
});

// =======================
// SAVE (FIXED)
// =======================
document.querySelector("#saveBtn").addEventListener("click", async (e) => {
    e.preventDefault();

    const payload = {
        currentEmpTIN: empTin.value,
        currentEmpName: empName.value,
        currentEmpAddress: empAddress.value,
        empContactDetails: empContact.value
    };

    console.log("SENDING PAYLOAD:", payload);

    const url = taxpayerId
        ? `http://localhost:8080/currentEmp/${taxpayerId}`
        : `http://localhost:8080/register/currentEmp`;

    const method = taxpayerId ? "PUT" : "POST";

    const res = await fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
    });

    if (!res.ok) {
        console.error(await res.text());
        alert("Save failed");
        return;
    }

    alert("Saved!");
setDisabled(true);
});

// =======================
// DELETE (FIXED)
// =======================
document.querySelector("#deleteBtn").addEventListener("click", async (e) => {
    e.preventDefault();

    if (!taxpayerId) {
        alert("No taxpayer ID found");
        return;
    }

    await fetch(`http://localhost:8080/currentEmp/${taxpayerId}`, {
        method: "DELETE"
    });

    alert("Deleted!");
    window.location.href = "1ccurrent.html";
});