<template>
    <div>
        <h2 id="page-heading">
            <span v-text="$t('gdb3App.arzt.home.title')" id="arzt-heading">Arzts</span>
            <router-link :to="{name: 'ArztCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-arzt">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span  v-text="$t('gdb3App.arzt.home.createLabel')">
                    Create a new Arzt
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
                            v-bind:placeholder="$t('gdb3App.arzt.home.search')"
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
        <div class="alert alert-warning" v-if="!isFetching && arzts && arzts.length === 0">
            <span v-text="$t('gdb3App.arzt.home.notFound')">No arzts found</span>
        </div>
        <div class="table-responsive" v-if="arzts && arzts.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><span v-text="$t('global.field.id')">ID</span></th>
                    <th><span v-text="$t('gdb3App.arzt.lanr')">Lanr</span></th>
                    <th><span v-text="$t('gdb3App.arzt.titel')">Titel</span></th>
                    <th><span v-text="$t('gdb3App.arzt.vorname')">Vorname</span></th>
                    <th><span v-text="$t('gdb3App.arzt.nachname')">Nachname</span></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="arzt in arzts"
                    :key="arzt.id">
                    <td>
                        <router-link :to="{name: 'ArztView', params: {arztId: arzt.id}}">{{arzt.id}}</router-link>
                    </td>
                    <td>{{arzt.lanr}}</td>
                    <td>{{arzt.titel}}</td>
                    <td>{{arzt.vorname}}</td>
                    <td>{{arzt.nachname}}</td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'ArztView', params: {arztId: arzt.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                            </router-link>
                            <router-link :to="{name: 'ArztEdit', params: {arztId: arzt.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(arzt)"
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
            <span slot="modal-title"><span id="gdb3App.arzt.delete.question" v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-arzt-heading" v-bind:title="$t('gdb3App.arzt.delete.question')">Are you sure you want to delete this Arzt?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-arzt" v-text="$t('entity.action.delete')" v-on:click="removeArzt()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./arzt.component.ts">
</script>
