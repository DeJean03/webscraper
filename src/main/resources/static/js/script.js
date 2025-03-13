document.addEventListener("DOMContentLoaded", () => {
    /** URL validation and scrape form handling **/
    const scrapeForm = document.getElementById("scrapeForm");
    const urlInput = document.getElementById("url");
    const urlError = document.getElementById("urlError");

    // Modern and robust URL validation
    const urlPattern = new RegExp(
        /^(https?:\/\/)([^\s$.?#].[^\s]*)$/i // Matches starting http/https followed by valid URL components
    );

    scrapeForm?.addEventListener("submit", (event) => {
        // Clear previous error message
        urlError.textContent = "";

        // Validate URL
        if (!urlPattern.test(urlInput.value)) {
            event.preventDefault(); // Prevent form submission
            urlError.textContent = "Please enter a valid URL starting with http:// or https://.";
            urlError.style.color = "red";
            return;
        }

        alert("Scraping request submitted!");
    });

    /** Dropdown display logic **/
    const dropdown = document.querySelector(".dropdown");
    dropdown?.addEventListener("click", () => {
        const content = document.querySelector(".dropdown-content");
        content?.classList.toggle("active"); // Use CSS class to toggle instead of inline style
    });

    /** Trigger scraping request **/
    async function triggerScraping() {
        const scrapeUrl = 'http://localhost:8080/scraper/start';
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
    triggerScraping(); // Call scraping function

    /** Registration form handling **/
    const registerForm = document.getElementById("registerForm");
    registerForm?.addEventListener("submit", async (event) => {
        event.preventDefault();

        const user = {
            username: document.getElementById("username").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value,
        };

        try {
            const response = await fetch("/users/create", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(user),
            });

            if (response.ok) {
                alert("User registered successfully!");
                window.location.href = "/dashboard"; // Redirect if successful
            } else {
                const error = await response.json();
                alert(error.message || "Registration failed.");
            }
        } catch (error) {
            console.error("Error during registration:", error.message);
            alert("An error occurred. Please try again.");
        }
    });
});