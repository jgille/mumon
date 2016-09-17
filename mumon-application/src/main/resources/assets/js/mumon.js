function populate(templateName, context) {
    $.get('templates/' + templateName + '.hb', function (data) {
        var template = Handlebars.compile(data);
        var html = template(context);
        $('#content-placeholder').html(html);
    })
}

function onHashChanged() {
    var serviceMatch = /services\/(\w+)/.exec(location.hash)
    if (serviceMatch) {
        var serviceName = serviceMatch[1];
        loadService(serviceName);
    } else {
        loadDashboard();
    }
}

function loadService(serviceName) {
    populate('service', { name: serviceName, instances: [ { id: 'instance_one'}, { id: 'instanceTwo'}]} );
}

function loadDashboard() {
    populate('dashboard', { services : [ {name: 'submission_service'}, {name: 'review-service'} ]});
}

$(window).on('hashchange', function() {
    var serviceMatch = /services\/(\w+)/.exec(location.hash)
    if (serviceMatch) {
        var serviceId = serviceMatch[1];
        loadService(serviceId);
    } else {
        loadDashboard();
    }
})

$(window).trigger('hashchange');

