/**
 * XML Parser & Dynamic Table Renderer
 * Directly demonstrates XML-Based Data Exchange (Lab Experiment 7)
 */

function loadAssignmentsXml() {
    const contextPath = document.body.dataset.contextPath || '';
    const container = document.getElementById('xmlOutputContainer');
    const rawXmlBox = document.getElementById('rawXmlDisplay');
    const statusMsg = document.getElementById('xmlStatusMsg');

    if (!container) return;

    statusMsg.innerHTML = '<span class="text-muted">Fetching XML from server (<code>/api/assignments.xml</code>)...</span>';

    fetch(`${contextPath}/api/assignments.xml`)
        .then(response => {
            if (!response.ok) throw new Error('Network response was not ok');
            return response.text();
        })
        .then(xmlString => {
            // Display Raw XML in pre block
            if (rawXmlBox) {
                rawXmlBox.textContent = xmlString;
            }

            // Parse XML Document using DOMParser
            const parser = new DOMParser();
            const xmlDoc = parser.parseFromString(xmlString, 'text/xml');

            const parseError = xmlDoc.querySelector('parsererror');
            if (parseError) {
                throw new Error('XML parsing error: ' + parseError.textContent);
            }

            const assignmentNodes = xmlDoc.getElementsByTagName('assignment');
            const total = xmlDoc.documentElement.getAttribute('total') || assignmentNodes.length;

            statusMsg.innerHTML = `<span class="text-success fw-bold">✓ Successfully fetched & parsed XML document with ${total} records!</span>`;

            if (assignmentNodes.length === 0) {
                container.innerHTML = '<div class="alert alert-info">No assignment elements found in XML.</div>';
                return;
            }

            // Render XML Data into an HTML Table
            let tableHtml = `
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Assignment Title</th>
                                <th>Subject</th>
                                <th>Faculty</th>
                                <th>Due Date</th>
                                <th>Max Marks</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
            `;

            for (let i = 0; i < assignmentNodes.length; i++) {
                const node = assignmentNodes[i];
                const id = node.getAttribute('id') || '-';
                const title = getNodeValue(node, 'title');
                const subjectCode = getNodeValue(node, 'subjectCode');
                const subjectName = getNodeValue(node, 'subjectName');
                const facultyName = getNodeValue(node, 'facultyName');
                const dueDate = getNodeValue(node, 'dueDate');
                const dueTime = getNodeValue(node, 'dueTime');
                const maxMarks = getNodeValue(node, 'maxMarks');
                const status = getNodeValue(node, 'status');

                tableHtml += `
                    <tr>
                        <td><span class="badge badge-active">${id}</span></td>
                        <td><strong>${escapeHtml(title)}</strong></td>
                        <td><span class="badge badge-submitted">${escapeHtml(subjectCode)}</span> ${escapeHtml(subjectName)}</td>
                        <td>${escapeHtml(facultyName)}</td>
                        <td>${dueDate} ${dueTime}</td>
                        <td><strong>${maxMarks} pts</strong></td>
                        <td><span class="badge badge-${status.toLowerCase()}">${status}</span></td>
                    </tr>
                `;
            }

            tableHtml += `
                        </tbody>
                    </table>
                </div>
            `;

            container.innerHTML = tableHtml;
        })
        .catch(error => {
            statusMsg.innerHTML = `<span class="text-danger fw-bold">✗ Error loading XML: ${error.message}</span>`;
            container.innerHTML = `<div class="alert alert-danger">Failed to process XML: ${error.message}</div>`;
        });
}

function loadTimetableXml() {
    const contextPath = document.body.dataset.contextPath || '';
    const container = document.getElementById('xmlOutputContainer');
    const rawXmlBox = document.getElementById('rawXmlDisplay');
    const statusMsg = document.getElementById('xmlStatusMsg');

    statusMsg.innerHTML = '<span class="text-muted">Fetching Timetable XML from server (<code>/api/timetable.xml</code>)...</span>';

    fetch(`${contextPath}/api/timetable.xml`)
        .then(response => response.text())
        .then(xmlString => {
            if (rawXmlBox) rawXmlBox.textContent = xmlString;

            const parser = new DOMParser();
            const xmlDoc = parser.parseFromString(xmlString, 'text/xml');
            const subjectNodes = xmlDoc.getElementsByTagName('subject');

            statusMsg.innerHTML = `<span class="text-success fw-bold">✓ Successfully parsed Curriculum/Timetable XML with ${subjectNodes.length} courses!</span>`;

            let tableHtml = `
                <div class="table-responsive">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Subject Code</th>
                                <th>Course Title</th>
                                <th>Semester</th>
                                <th>Department</th>
                            </tr>
                        </thead>
                        <tbody>
            `;

            for (let i = 0; i < subjectNodes.length; i++) {
                const node = subjectNodes[i];
                const code = getNodeValue(node, 'code');
                const name = getNodeValue(node, 'name');
                const semester = getNodeValue(node, 'semester');
                const department = getNodeValue(node, 'department');

                tableHtml += `
                    <tr>
                        <td><span class="badge badge-submitted">${escapeHtml(code)}</span></td>
                        <td><strong>${escapeHtml(name)}</strong></td>
                        <td>Semester ${semester}</td>
                        <td>${escapeHtml(department)}</td>
                    </tr>
                `;
            }

            tableHtml += `
                        </tbody>
                    </table>
                </div>
            `;

            container.innerHTML = tableHtml;
        })
        .catch(err => {
            statusMsg.innerHTML = `<span class="text-danger fw-bold">✗ Error loading Timetable XML: ${err.message}</span>`;
        });
}

function getNodeValue(parentNode, tagName) {
    const el = parentNode.getElementsByTagName(tagName)[0];
    return el && el.textContent ? el.textContent : '-';
}

function escapeHtml(str) {
    if (!str) return '';
    return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;").replace(/"/g, "&quot;").replace(/'/g, "&#039;");
}
