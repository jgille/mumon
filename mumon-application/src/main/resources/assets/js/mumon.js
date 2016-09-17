function populate(templateName, context) {
    $.get('templates/' + templateName + '.hb', function (data) {
        var template = Handlebars.compile(data);
        var html = template(context);
        $('#content-placeholder').html(html);
    })
}

function loadService(serviceName) {
    $.get('api/services/' + serviceName, function (data) {
        for (i = 0; i < data.instances.length; i++) {
            var formatted = moment(data.instances[i].timestamp).format("ddd, MMM Do, HH:mm:ss");
            data.instances[i].timestamp = formatted;
        }
        populate('service', data);
    });
}

function loadDashboard() {
    $.get('api/services', function (data) {
       populate('dashboard', data);
    });
}

$(window).on('hashchange', function() {
    var serviceMatch = /services\/(.+)/.exec(location.hash)
    if (serviceMatch) {
        var serviceId = serviceMatch[1];
        loadService(serviceId);
    } else {
        loadDashboard();
    }
})

$(window).trigger('hashchange');

