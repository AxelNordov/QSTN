$("#myModal").on('show.bs.modal', function (e) {
    var wordCardId = $(e.relatedTarget).data('wordCard-id');
    var cols = $('#wordCard-' + wordCardId + ' td');
    var text = $(cols[0]).text();
    var translated = $(cols[1]).text();
    var language = $(cols[2]).text();
    $('#textInput').val(text);
    $('#translatedInput').val(translated);
    $('#languageInput').val(language);
});

$("#myModal").on('hidden.bs.modal', function () {
    var form = $(this).find('form');
    form[0].reset();
});