<template>
    <div>
        <h2 id="page-heading">
            <span v-text="$t('gdb3App.systeminstanz.home.title')" id="systeminstanz-heading">Systeminstanzs</span>
            <router-link :to="{name: 'SysteminstanzCreate'}" tag="button" id="jh-create-entity" class="btn btn-primary float-right jh-create-entity create-systeminstanz">
                <font-awesome-icon icon="plus"></font-awesome-icon>
                <span  v-text="$t('gdb3App.systeminstanz.home.createLabel')">
                    Create a new Systeminstanz
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
                            v-bind:placeholder="$t('gdb3App.systeminstanz.home.search')"
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
        <div class="alert alert-warning" v-if="!isFetching && systeminstanzs && systeminstanzs.length === 0">
            <span v-text="$t('gdb3App.systeminstanz.home.notFound')">No systeminstanzs found</span>
        </div>
        <div class="table-responsive" v-if="systeminstanzs && systeminstanzs.length > 0">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th><span v-text="$t('global.field.id')">ID</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.bezeichnung')">Bezeichnung</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.geraetNummer')">Geraet Nummer</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.geraetBaujahr')">Geraet Baujahr</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.gueltigBis')">Gueltig Bis</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.gwe')">Gwe</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.bemerkung')">Bemerkung</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.systemtyp')">Systemtyp</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.betriebsstaette')">Betriebsstaette</span></th>
                    <th><span v-text="$t('gdb3App.systeminstanz.betreiber')">Betreiber</span></th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="systeminstanz in systeminstanzs"
                    :key="systeminstanz.id">
                    <td>
                        <router-link :to="{name: 'SysteminstanzView', params: {systeminstanzId: systeminstanz.id}}">{{systeminstanz.id}}</router-link>
                    </td>
                    <td>{{systeminstanz.bezeichnung}}</td>
                    <td>{{systeminstanz.geraetNummer}}</td>
                    <td>{{systeminstanz.geraetBaujahr}}</td>
                    <td>{{systeminstanz.gueltigBis}}</td>
                    <td>
                        <a v-if="systeminstanz.gwe" v-on:click="openFile(systeminstanz.gweContentType, systeminstanz.gwe)">
                            <img v-bind:src="'data:' + systeminstanz.gweContentType + ';base64,' + systeminstanz.gwe" style="max-height: 30px;" alt="systeminstanz image"/>
                        </a>
                        <span v-if="systeminstanz.gwe">{{systeminstanz.gweContentType}}, {{byteSize(systeminstanz.gwe)}}</span>
                    </td>
                    <td>{{systeminstanz.bemerkung}}</td>
                    <td>
                        <div v-if="systeminstanz.systemtypId">
                            <router-link :to="{name: 'SystemtypView', params: {systemtypId: systeminstanz.systemtypId}}">{{systeminstanz.systemtypBezeichnung}}</router-link>
                        </div>
                    </td>
                    <td>
                        <div v-if="systeminstanz.betriebsstaetteId">
                            <router-link :to="{name: 'BetriebsstaetteView', params: {betriebsstaetteId: systeminstanz.betriebsstaetteId}}">{{systeminstanz.betriebsstaetteId}}</router-link>
                        </div>
                    </td>
                    <td>
                        <div v-if="systeminstanz.betreiberId">
                            <router-link :to="{name: 'BetreiberView', params: {betreiberId: systeminstanz.betreiberId}}">{{systeminstanz.betreiberId}}</router-link>
                        </div>
                    </td>
                    <td class="text-right">
                        <div class="btn-group">
                            <router-link :to="{name: 'SysteminstanzView', params: {systeminstanzId: systeminstanz.id}}" tag="button" class="btn btn-info btn-sm details">
                                <font-awesome-icon icon="eye"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                            </router-link>
                            <router-link :to="{name: 'SysteminstanzEdit', params: {systeminstanzId: systeminstanz.id}}"  tag="button" class="btn btn-primary btn-sm edit">
                                <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                                <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                            </router-link>
                            <b-button v-on:click="prepareRemove(systeminstanz)"
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
            <span slot="modal-title"><span id="gdb3App.systeminstanz.delete.question" v-text="$t('entity.delete.title')">Confirm delete operation</span></span>
            <div class="modal-body">
                <p id="jhi-delete-systeminstanz-heading" v-bind:title="$t('gdb3App.systeminstanz.delete.question')">Are you sure you want to delete this Systeminstanz?</p>
            </div>
            <div slot="modal-footer">
                <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
                <button type="button" class="btn btn-primary" id="jhi-confirm-delete-systeminstanz" v-text="$t('entity.action.delete')" v-on:click="removeSysteminstanz()">Delete</button>
            </div>
        </b-modal>
    </div>
</template>

<script lang="ts" src="./systeminstanz.component.ts">
</script>
