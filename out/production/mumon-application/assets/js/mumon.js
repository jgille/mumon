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
    var data = { name: serviceName, instances: [
        {
            id: 'instance_one',
            status: 'CRITICAL',
            service_version: '0.0.3',
            host_address: 'http://123.23.23.56',
            health_checks: [
                { name: 'db', status: 'CRITICAL', message: 'Read timed out' },
                { name: 'ldap', status: 'HEALTHY' }
            ]
        },
        {
            id: 'instance_two',
            status: 'HEALTHY',
            service_version: '0.0.3',
            host_address: 'http://123.23.23.84',
            health_checks: [
                { name: 'db', status: 'HEALTHY' },
                { name: 'ldap', status: 'HEALTHY' }
            ]
        }
    ]};
    populate('service', data);
}

function loadDashboard() {
    var data = { services : [
        {name: 'submission_service', status: 'CRITICAL'},
        {name: 'review_service', status: 'WARNING'},
        {name: 'recommendation_service', status: 'HEALTHY'}
    ]};
    populate('dashboard', data);
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

