<div id="service" class="service">
    <h1 class="heading"><span class="{{status}} label">{{name}}</span></h1>
    {{#each instances}}
    <div class="col-lg-6">
        <div class="panel panel-default">
            <div class="panel-heading">
                <h2>{{id}}<span class="pull-right {{status}} label">{{status}}</span></h2>
            </div>
            <div class="panel-body">
                <table class="table">
                    <tr>
                        <td>Service Version</td>
                        <td>{{service_version}}</td>
                    </tr>
                    <tr>
                        <td>Host Address</td>
                        <td>{{host_address}}</td>
                    </tr>
                    <tr>
                        <td>Time</td>
                        <td>{{timestamp}}</td>
                    </tr>
                </table>
                <table class="table table-bordered health_checks">
                    {{#each health_checks}}
                    <tr>
                        <td>
                            <div class="label {{status}} col-lg-12">{{name}}</div>
                        </td>
                        {{#if message}}
                        <td>{{message}}</td>
                        {{/if}}
                    </tr>
                    {{/each}}
                </table>
            </div>
        </div>
    </div>
    {{else}}
    <div class="heading alert alert-danger col-md-4"><h3>No instances</h3></div>
    {{/each}}
</div>