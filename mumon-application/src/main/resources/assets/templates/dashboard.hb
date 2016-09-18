<div id="dashboard">
    {{#each services}}
    <div class="dashboard col-sm-12 col-md-6 col-lg-4">
        <a href="#/services/{{name}}" class="{{status}} label col-xs-12">
            {{name}}
        </a>
    </div>
    {{else}}
    <div class="alert alert-warning col-md-4"><h3>No monitored services</h3></div>
    {{/each}}
</div>