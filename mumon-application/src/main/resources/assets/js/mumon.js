function populate(templateName, context) {
    $.get('templates/' + templateName + '.hb', function (data) {
        var template = Handlebars.compile(data);
        var html = template(context);
        $('#content-placeholder').html(html);
    })
}

populate('dashboard', {});