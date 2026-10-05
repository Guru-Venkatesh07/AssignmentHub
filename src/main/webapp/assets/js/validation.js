/**
 * Client-Side Validation & AJAX Live Verification
 * Demonstrates AJAX Form Validation (Lab Experiment 6)
 */

document.addEventListener('DOMContentLoaded', () => {
    const contextPath = document.body.dataset.contextPath || '';

    // ----------------------------------------------------------------
    // 1. AJAX Email Live Availability Check
    // ----------------------------------------------------------------
    const emailInput = document.getElementById('emailInput');
    const emailFeedback = document.getElementById('emailFeedback');

    if (emailInput && emailFeedback) {
        let debounceTimer;
        emailInput.addEventListener('input', () => {
            clearTimeout(debounceTimer);
            const email = emailInput.value.trim();
            if (!email || !/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                emailFeedback.textContent = 'Please enter a valid email address.';
                emailFeedback.className = 'form-text text-danger';
                return;
            }

            emailFeedback.textContent = 'Checking availability...';
            emailFeedback.className = 'form-text text-muted';

            debounceTimer = setTimeout(() => {
                fetch(`${contextPath}/api/check-email?email=${encodeURIComponent(email)}`)
                    .then(res => res.json())
                    .then(data => {
                        if (data.available) {
                            emailFeedback.textContent = '✓ Email is available!';
                            emailFeedback.className = 'form-text text-success fw-semibold';
                        } else {
                            emailFeedback.textContent = '✗ ' + data.message;
                            emailFeedback.className = 'form-text text-danger fw-semibold';
                        }
                    })
                    .catch(() => {
                        emailFeedback.textContent = '';
                    });
            }, 400);
        });
    }

    // ----------------------------------------------------------------
    // 2. AJAX Register Number Live Check
    // ----------------------------------------------------------------
    const regNoInput = document.getElementById('regNoInput');
    const regNoFeedback = document.getElementById('regNoFeedback');

    if (regNoInput && regNoFeedback) {
        let debounceTimer;
        regNoInput.addEventListener('input', () => {
            clearTimeout(debounceTimer);
            const regNo = regNoInput.value.trim();
            if (!regNo || regNo.length < 5) {
                regNoFeedback.textContent = 'Register Number must be at least 5 alphanumeric characters.';
                regNoFeedback.className = 'form-text text-danger';
                return;
            }

            regNoFeedback.textContent = 'Checking register number...';
            regNoFeedback.className = 'form-text text-muted';

            debounceTimer = setTimeout(() => {
                fetch(`${contextPath}/api/check-register-number?registerNumber=${encodeURIComponent(regNo)}`)
                    .then(res => res.json())
                    .then(data => {
                        if (data.available) {
                            regNoFeedback.textContent = '✓ Register Number is valid and available!';
                            regNoFeedback.className = 'form-text text-success fw-semibold';
                        } else {
                            regNoFeedback.textContent = '✗ ' + data.message;
                            regNoFeedback.className = 'form-text text-danger fw-semibold';
                        }
                    })
                    .catch(() => {
                        regNoFeedback.textContent = '';
                    });
            }, 400);
        });
    }

    // ----------------------------------------------------------------
    // 3. Password Match & Strength Validation
    // ----------------------------------------------------------------
    const passwordInput = document.getElementById('passwordInput');
    const confirmPasswordInput = document.getElementById('confirmPasswordInput');
    const passwordFeedback = document.getElementById('passwordFeedback');

    if (passwordInput && confirmPasswordInput && passwordFeedback) {
        const checkPasswords = () => {
            const p1 = passwordInput.value;
            const p2 = confirmPasswordInput.value;

            if (p1.length < 6) {
                passwordFeedback.textContent = 'Password must be at least 6 characters.';
                passwordFeedback.className = 'form-text text-danger';
                return;
            }

            if (p2 && p1 !== p2) {
                passwordFeedback.textContent = 'Passwords do not match.';
                passwordFeedback.className = 'form-text text-danger';
            } else if (p2 && p1 === p2) {
                passwordFeedback.textContent = '✓ Passwords match perfectly.';
                passwordFeedback.className = 'form-text text-success fw-semibold';
            } else {
                passwordFeedback.textContent = '';
            }
        };

        passwordInput.addEventListener('input', checkPasswords);
        confirmPasswordInput.addEventListener('input', checkPasswords);
    }

    // ----------------------------------------------------------------
    // 4. File Upload Validation (Size <= 10MB, Allowed Exts)
    // ----------------------------------------------------------------
    const fileInput = document.getElementById('fileUploadInput');
    const fileFeedback = document.getElementById('fileFeedback');
    const fileNameDisplay = document.getElementById('fileNameDisplay');

    if (fileInput && fileFeedback) {
        const allowedExtensions = ['pdf', 'docx', 'doc', 'pptx', 'ppt', 'zip'];
        const maxSizeBytes = 10 * 1024 * 1024; // 10MB

        fileInput.addEventListener('change', () => {
            const file = fileInput.files[0];
            if (!file) {
                if (fileNameDisplay) fileNameDisplay.textContent = '';
                return;
            }

            const fileName = file.name;
            const fileExt = fileName.split('.').pop().toLowerCase();

            if (fileNameDisplay) {
                fileNameDisplay.textContent = `Selected: ${fileName} (${(file.size / (1024 * 1024)).toFixed(2)} MB)`;
            }

            if (!allowedExtensions.includes(fileExt)) {
                fileFeedback.textContent = '✗ Invalid file format. Only PDF, DOCX, PPTX, and ZIP files are allowed.';
                fileFeedback.className = 'form-text text-danger fw-semibold';
                fileInput.value = '';
                return;
            }

            if (file.size > maxSizeBytes) {
                fileFeedback.textContent = `✗ File is too large (${(file.size / (1024 * 1024)).toFixed(2)} MB). Maximum allowed size is 10 MB.`;
                fileFeedback.className = 'form-text text-danger fw-semibold';
                fileInput.value = '';
                return;
            }

            fileFeedback.textContent = '✓ File is valid and ready for upload.';
            fileFeedback.className = 'form-text text-success fw-semibold';
        });
    }
});
