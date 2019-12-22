<template>
    <div>
        <h2 id="page-heading">
            <span v-text="$t('gdb3App.zubehoerTyp.home.title')" id="zubehoer-typ-heading">Zubehoer Typs</span>
            <router-link :to="{name: 'ZubehoerTypCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-zubehoer-typ">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span  v-text="$t('gdb3App.zubehoerTyp.home.createLabel')">
                    Create a new Zubehoer Typ
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
                            v-bind:placeholder="$t('gdb3App.zubehoerTyp.home.search')"
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
        <div class="alert alert-warning" v-if="!isFetching && zubehoerTyps && zubehoerTyps.length === 0">
            <span v-text="$t('gdb3App.zubehoerTyp.home.notFound')">No zubehoerTyps found</span>
        </div>
        <div class="table-responsive" v-if="zubehoerTyps && zubehoerTyps.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><span v-text="$t('global.field.id')">ID</span></th>
                    <th><span v-text="$t('gdb3App.zubehoerTyp.bezeichnung')">Bezeichnung</span></th>
                    <th><span v-text="$t('gdb3App.zubehoerTyp.gueltigBis')">Gueltig Bis</span></th>
                    <th><span v-text="$t('gdb3App.zubehoerTyp.technologie')">Technologie</span></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="zubehoerTyp in zubehoerTyps"
                    :key="zubehoerTyp.id">
                    <td>
                        <router-link :to="{name: 'ZubehoerTypView', params: {zubehoerTypId: zubehoerTyp.id}}">{{zubehoerTyp.id}}</router-link>
                    </td>
                    <td>{{zubehoerTyp.bezeichnung}}</td>
                    <td>{{zubehoerTyp.gueltigBis}}</td>
                    <td v-text="$t('gdb3App.Technologie.' + zubehoerTyp.technologie)">{{zubehoerTyp.technologie}}</td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'ZubehoerTypView', params: {zubehoerTypId: zubehoerTyp.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                            </router-link>
                            <router-link :to="{name: 'ZubehoerTypEdit', params: {zubehoerTypId: zubehoerTyp.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(zubehoerTyp)"
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
            <span slot="modal-title"><span id="gdb3App.zubehoerTyp.delete.question" v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-zubehoerTyp-heading" v-bind:title="$t('gdb3App.zubehoerTyp.delete.question')">Are you sure you want to delete this Zubehoer Typ?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-zubehoerTyp" v-text="$t('entity.action.delete')" v-on:click="removeZubehoerTyp()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./zubehoer-typ.component.ts">
</script>
