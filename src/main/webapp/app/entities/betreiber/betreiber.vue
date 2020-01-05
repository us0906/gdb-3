<template>
    <div>
        <h2 id="page-heading">
            <span v-text="$t('gdb3App.betreiber.home.title')" id="betreiber-heading">Betreibers</span>
            <router-link :to="{name: 'BetreiberCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-betreiber">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span  v-text="$t('gdb3App.betreiber.home.createLabel')">
                    Create a new Betreiber
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
                            v-bind:placeholder="$t('gdb3App.betreiber.home.search')"
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
        <div class="alert alert-warning" v-if="!isFetching && betreibers && betreibers.length === 0">
            <span v-text="$t('gdb3App.betreiber.home.notFound')">No betreibers found</span>
        </div>
        <div class="table-responsive" v-if="betreibers && betreibers.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><span v-text="$t('global.field.id')">ID</span></th>
                    <th><span v-text="$t('gdb3App.betreiber.vorname')">Vorname</span></th>
                    <th><span v-text="$t('gdb3App.betreiber.nachname')">Nachname</span></th>
                    <th><span v-text="$t('gdb3App.betreiber.strasse')">Strasse</span></th>
                    <th><span v-text="$t('gdb3App.betreiber.hausnummer')">Hausnummer</span></th>
                    <th><span v-text="$t('gdb3App.betreiber.plz')">Plz</span></th>
                    <th><span v-text="$t('gdb3App.betreiber.ort')">Ort</span></th>

                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="betreiber in betreibers"
                    :key="betreiber.id">
                    <td>
                        <router-link :to="{name: 'BetreiberView', params: {betreiberId: betreiber.id}}">{{betreiber.id}}</router-link>
                    </td>
                    <td>{{betreiber.vorname}}</td>
                    <td>{{betreiber.nachname}}</td>
                    <td>{{betreiber.strasse}}</td>
                    <td>{{betreiber.hausnummer}}</td>
                    <td>{{betreiber.plz}}</td>
                    <td>{{betreiber.ort}}</td>

                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'BetreiberView', params: {betreiberId: betreiber.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                            </router-link>
                            <router-link :to="{name: 'BetreiberEdit', params: {betreiberId: betreiber.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(betreiber)"
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
            <span slot="modal-title"><span id="gdb3App.betreiber.delete.question" v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-betreiber-heading" v-bind:title="$t('gdb3App.betreiber.delete.question')">Are you sure you want to delete this Betreiber?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-betreiber" v-text="$t('entity.action.delete')" v-on:click="removeBetreiber()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./betreiber.component.ts">
</script>
