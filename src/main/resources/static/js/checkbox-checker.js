var maxSelected = 4;
(function () {
    const form = document.querySelector('#sectionForm');
    const checkboxes = form.querySelectorAll('input[type=checkbox]');
    const checkboxLength = checkboxes.length;
    const firstCheckbox = checkboxLength > 0 ? checkboxes[0] : null;

    function init() {
        if (firstCheckbox) {
            for (let i = 0; i < checkboxLength; i++) {
                checkboxes[i].addEventListener('change', checkValidity);
            }
            checkValidity();
        }
    }

    function isChecked() {
        var checked = 0;
        for (let i = 0; i < checkboxLength; i++) {
            if (checkboxes[i].checked) checked++;
        }
        return checked === maxSelected;
    }

    function checkValidity() {
        const errorMessage = !isChecked() ? 'Musisz wybrać 4 kategorie' : '';
        firstCheckbox.setCustomValidity(errorMessage);
    }

    init();
})();
