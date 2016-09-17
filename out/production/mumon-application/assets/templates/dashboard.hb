<div id="dashboard">
    {{#each services}}
    <div class="dashboard col-sm-12 col-md-6 col-lg-4">
        <a href="#/services/{{name}}" class="{{status}} label col-xs-12">
            {{name}}
        </a>
    </div>
    {{/each}}
</div>