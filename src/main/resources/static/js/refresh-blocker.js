function disableF5(e) {
    if ((e.which || e.keyCode) == 116) e.preventDefault();
};
$(document).on("keydown", disableF5);