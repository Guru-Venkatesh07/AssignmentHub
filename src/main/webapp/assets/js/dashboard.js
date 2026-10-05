/**
 * Dashboard Live AJAX Statistics Refresh
 * Updates dashboard cards without reloading the entire page.
 */

function refreshStudentDashboard() {
    const contextPath = document.body.dataset.contextPath || '';
    const refreshBtn = document.getElementById('refreshStatsBtn');
    if (refreshBtn) refreshBtn.classList.add('spinning');

    fetch(`${contextPath}/api/student/dashboard`)
        .then(res => res.json())
        .then(stats => {
            if (document.getElementById('statTotalAssignments')) {
                document.getElementById('statTotalAssignments').textContent = stats.totalAssignments;
            }
            if (document.getElementById('statSubmitted')) {
                document.getElementById('statSubmitted').textContent = stats.submitted;
            }
            if (document.getElementById('statPending')) {
                document.getElementById('statPending').textContent = stats.pending;
            }
            if (document.getElementById('statEvaluated')) {
                document.getElementById('statEvaluated').textContent = stats.evaluated;
            }
            if (document.getElementById('statLate')) {
                document.getElementById('statLate').textContent = stats.late;
            }
            if (document.getElementById('statAverageMarks')) {
                document.getElementById('statAverageMarks').textContent = stats.averageMarks + '%';
            }
        })
        .catch(err => console.error('Error updating dashboard stats:', err))
        .finally(() => {
            if (refreshBtn) refreshBtn.classList.remove('spinning');
        });
}

function refreshFacultyDashboard() {
    const contextPath = document.body.dataset.contextPath || '';
    const refreshBtn = document.getElementById('refreshStatsBtn');
    if (refreshBtn) refreshBtn.classList.add('spinning');

    fetch(`${contextPath}/api/faculty/dashboard`)
        .then(res => res.json())
        .then(stats => {
            if (document.getElementById('statCreated')) {
                document.getElementById('statCreated').textContent = stats.assignmentsCreated;
            }
            if (document.getElementById('statTotalSubs')) {
                document.getElementById('statTotalSubs').textContent = stats.totalSubmissions;
            }
            if (document.getElementById('statPendingEval')) {
                document.getElementById('statPendingEval').textContent = stats.pendingEvaluations;
            }
            if (document.getElementById('statEvaluatedSubs')) {
                document.getElementById('statEvaluatedSubs').textContent = stats.evaluatedSubmissions;
            }
            if (document.getElementById('statLateSubs')) {
                document.getElementById('statLateSubs').textContent = stats.lateSubmissions;
            }
        })
        .catch(err => console.error('Error updating faculty dashboard stats:', err))
        .finally(() => {
            if (refreshBtn) refreshBtn.classList.remove('spinning');
        });
}
