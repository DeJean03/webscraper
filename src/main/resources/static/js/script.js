document.getElementById("signupForm").addEventListener("submit", function(event) {
    event.preventDefault(); // Prevent the form from submitting
    // Clear all previous error messages
    document.querySelectorAll(".error-message").forEach(el => el.style.display = "none");

    const name = document.getElementById("name").value.trim();
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();
    const refer = document.getElementById("refer").value;
    const terms = document.getElementById("terms").checked;

    let valid = true;

    if (!name) {
        valid = false;
        document.getElementById("nameError").style.display = "block";
    }

    if (!validateEmail(email)) {
        valid = false;
        document.getElementById("emailError").style.display = "block";
    }

    if (!validatePassword(password)) {
        valid = false;
        document.getElementById("passwordError").style.display = "block";
    }

    if (!refer) {
        valid = false;
        document.getElementById("referError").style.display = "block";
    }

    if (!terms) {
        valid = false;
        document.getElementById("termsError").style.display = "block";
    }

    if (valid) {
        this.submit();
    }
});

function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validatePassword(password) {
    const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[A-Za-z\d]{8,}$/;
    return re.test(password);
}
document.addEventListener("DOMContentLoaded", () => {
    const dropdown = document.querySelector(".dropdown");
    dropdown.addEventListener("click", () => {
        const content = document.querySelector(".dropdown-content");
        content.classList.toggle("active");
    });

    async function triggerScraping() {
        const scrapeUrl = 'http://localhost:63342/scraper/start';
        const payload = { url: 'http://localhost:63342/webscraper/templates/scraping-results' };

        try {
            const response = await fetch(scrapeUrl, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(payload),
            });

            const result = await response.text();
            console.log("Scraping response:", result);
        } catch (error) {
            console.error("CORS error during scraping:", error.message);
        }
    }
    triggerScraping();
});
