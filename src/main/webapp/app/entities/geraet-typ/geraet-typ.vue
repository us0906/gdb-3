<template>
    <div>
        <h2 id="page-heading">
            <span v-text="$t('gdb3App.geraetTyp.home.title')" id="geraet-typ-heading">Geraet Typs</span>
            <router-link :to="{name: 'GeraetTypCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-geraet-typ">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span  v-text="$t('gdb3App.geraetTyp.home.createLabel')">
                    Create a new Geraet Typ
                </span>
            </router-link>
        </h2>
        <b-alert :show="dismissCountDown"
            dismissible
            :variant="alertType"
            @dismissed="dismissCountDown=0"
            @dismiss-count-down="countDownChanged">
            {{alertMessage}}
        </b-alert>
        <div class="row">
            <div class="col-sm-12">
                <form name="searchForm" class="form-inline" v-on:submit.prevent="search(currentSearch)">
                    <div class="input-group w-100 mt-3">
                        <input type="text" class="form-control" name="currentSearch" id="currentSearch"
                            v-bind:placeholder="$t('gdb3App.geraetTyp.home.search')"
                            v-model="currentSearch" />
                        <button type="button" id="launch-search" class="btn btn-primary" v-on:click="search(currentSearch)">
                            <font-awesome-icon icon="search"></font-awesome-icon>
                        </button>
                        <button type="button" id="clear-search" class="btn btn-secondary" v-on:click="clear()"
                            v-if="currentSearch">
                            <font-awesome-icon icon="trash"></font-awesome-icon>
                        </button>
                    </div>
                </form>
            </div>
        </div>
        <br/>
        <div class="alert alert-warning" v-if="!isFetching && geraetTyps && geraetTyps.length === 0">
            <span v-text="$t('gdb3App.geraetTyp.home.notFound')">No geraetTyps found</span>
        </div>
        <div class="table-responsive" v-if="geraetTyps && geraetTyps.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><span v-text="$t('global.field.id')">ID</span></th>
                    <th><span v-text="$t('gdb3App.geraetTyp.bezeichnung')">Bezeichnung</span></th>
                    <th><span v-text="$t('gdb3App.geraetTyp.gueltigBis')">Gueltig Bis</span></th>
                    <th><span v-text="$t('gdb3App.geraetTyp.technologie')">Technologie</span></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="geraetTyp in geraetTyps"
                    :key="geraetTyp.id">
                    <td>
                        <router-link :to="{name: 'GeraetTypView', params: {geraetTypId: geraetTyp.id}}">{{geraetTyp.id}}</router-link>
                    </td>
                    <td>{{geraetTyp.bezeichnung}}</td>
                    <td>{{geraetTyp.gueltigBis}}</td>
                    <td v-text="$t('gdb3App.Technologie.' + geraetTyp.technologie)">{{geraetTyp.technologie}}</td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'GeraetTypView', params: {geraetTypId: geraetTyp.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                            </router-link>
                            <router-link :to="{name: 'GeraetTypEdit', params: {geraetTypId: geraetTyp.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(geraetTyp)"
                                   variant="danger"
                                   class="btn btn-sm"
                                   v-b-modal.removeEntity>
                                <font-awesome-icon icon="times"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                            </b-button>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
        <b-modal ref="removeEntity" id="removeEntity" >
            <span slot="modal-title"><span id="gdb3App.geraetTyp.delete.question" v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-geraetTyp-heading" v-bind:title="$t('gdb3App.geraetTyp.delete.question')">Are you sure you want to delete this Geraet Typ?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-geraetTyp" v-text="$t('entity.action.delete')" v-on:click="removeGeraetTyp()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./geraet-typ.component.ts">
</script>
