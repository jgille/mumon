<div id="service">
    <h1>{{name}}</h1>
    <div class="col-lg-6">
        {{#each instances}}
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
                </table>
                <table class="table table-bordered health_checks">
                    {{#each health_checks}}
                    <tr>
                        <td><div class="label {{status}} col-lg-12">{{name}}</div></td>
                        {{#if message}}
                        <td>{{message}}</td>
                        {{/if}}
                    </tr>
                    {{/each}}
                </table>
            </div>
        </div>
        {{/each}}
    </div>
</div>