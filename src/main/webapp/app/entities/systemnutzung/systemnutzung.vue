<template>
    <div>
        <h2 id="page-heading">
            <span v-text="$t('gdb3App.systemnutzung.home.title')" id="systemnutzung-heading">Systemnutzungs</span>
            <router-link :to="{name: 'SystemnutzungCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-systemnutzung">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span  v-text="$t('gdb3App.systemnutzung.home.createLabel')">
                    Create a new Systemnutzung
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
                            v-bind:placeholder="$t('gdb3App.systemnutzung.home.search')"
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
        <div class="alert alert-warning" v-if="!isFetching && systemnutzungs && systemnutzungs.length === 0">
            <span v-text="$t('gdb3App.systemnutzung.home.notFound')">No systemnutzungs found</span>
        </div>
        <div class="table-responsive" v-if="systemnutzungs && systemnutzungs.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><span v-text="$t('global.field.id')">ID</span></th>
                    <th><span v-text="$t('gdb3App.systemnutzung.systeminstanz')">Systeminstanz</span></th>
                    <th><span v-text="$t('gdb3App.systemnutzung.arzt')">Arzt</span></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="systemnutzung in systemnutzungs"
                    :key="systemnutzung.id">
                    <td>
                        <router-link :to="{name: 'SystemnutzungView', params: {systemnutzungId: systemnutzung.id}}">{{systemnutzung.id}}</router-link>
                    </td>
                    <td>
                        <div v-if="systemnutzung.systeminstanzId">
                            <router-link :to="{name: 'SysteminstanzView', params: {systeminstanzId: systemnutzung.systeminstanzId}}">{{systemnutzung.systeminstanzBezeichnung}}</router-link>
                        </div>
                    </td>
                    <td>
                        <div v-if="systemnutzung.arztId">
                            <router-link :to="{name: 'ArztView', params: {arztId: systemnutzung.arztId}}">{{systemnutzung.arztBezeichnung}}</router-link>
                        </div>
                    </td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'SystemnutzungView', params: {systemnutzungId: systemnutzung.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                            </router-link>
                            <router-link :to="{name: 'SystemnutzungEdit', params: {systemnutzungId: systemnutzung.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(systemnutzung)"
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
            <span slot="modal-title"><span id="gdb3App.systemnutzung.delete.question" v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-systemnutzung-heading" v-bind:title="$t('gdb3App.systemnutzung.delete.question')">Are you sure you want to delete this Systemnutzung?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-systemnutzung" v-text="$t('entity.action.delete')" v-on:click="removeSystemnutzung()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./systemnutzung.component.ts">
</script>
