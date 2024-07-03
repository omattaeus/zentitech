document.addEventListener("DOMContentLoaded", function () {
    const stripe = Stripe(publicKey);

    // Event listener for form submission
    document.querySelector("#paymentForm").addEventListener("submit", handlePayment);

    async function handlePayment(event) {
        event.preventDefault();
        setLoading(true);

        const emailField = document.querySelector("#email");
        const productNameField = document.querySelector("#productName");
        const amountInCents = 1490;

        const email = emailField.value;
        const productName = productNameField.value;

        const request = {
            amount: amountInCents,
            email: email,
            productName: productName
        };

        try {
            const response = await fetch("/create-payment-intent", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(request)
            });

            if (!response.ok) {
                throw new Error('Erro ao criar sessão de checkout');
            }

            const checkoutSession = await response.json();

            stripe.redirectToCheckout({
                sessionId: checkoutSession.sessionId
            }).then(function (result) {
                if (result.error) {
                    showMessage(result.error.message);
                }
                setLoading(false);
            });
        } catch (error) {
            console.error('Erro ao iniciar checkout:', error);
            setLoading(false);
            showMessage('Erro ao iniciar checkout. Por favor, tente novamente mais tarde.');
        }
    }

    // UI helpers
    function setLoading(isLoading) {
        if (isLoading) {
            document.querySelector("#submit").disabled = true;
            document.querySelector("#spinner").classList.remove("hidden");
            document.querySelector("#button-text").classList.add("hidden");
        } else {
            document.querySelector("#submit").disabled = false;
            document.querySelector("#spinner").classList.add("hidden");
            document.querySelector("#button-text").classList.remove("hidden");
        }
    }

    function showMessage(messageText) {
        const messageContainer = document.querySelector("#payment-message");

        messageContainer.classList.remove("hidden");
        messageContainer.textContent = messageText;

        setTimeout(function () {
            messageContainer.classList.add("hidden");
            messageContainer.textContent = "";
        }, 4000);
    }
});