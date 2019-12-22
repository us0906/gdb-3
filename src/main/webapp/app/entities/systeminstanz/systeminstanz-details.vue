<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="systeminstanz">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.systeminstanz.detail.title')">Systeminstanz</span> {{systeminstanz.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.bezeichnung')">Bezeichnung</span>
                    </dt>
                    <dd>
                        <span>{{systeminstanz.bezeichnung}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.geraetNummer')">Geraet Nummer</span>
                    </dt>
                    <dd>
                        <span>{{systeminstanz.geraetNummer}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.geraetBaujahr')">Geraet Baujahr</span>
                    </dt>
                    <dd>
                        <span>{{systeminstanz.geraetBaujahr}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.gueltigBis')">Gueltig Bis</span>
                    </dt>
                    <dd>
                        <span>{{systeminstanz.gueltigBis}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.gwe')">Gwe</span>
                    </dt>
                    <dd>
                        <div v-if="systeminstanz.gwe">
                            <a v-on:click="openFile(systeminstanz.gweContentType, systeminstanz.gwe)">
                                <img v-bind:src="'data:' + systeminstanz.gweContentType + ';base64,' + systeminstanz.gwe" style="max-width: 100%;" alt="systeminstanz image"/>
                            </a>
                            {{systeminstanz.gweContentType}}, {{byteSize(systeminstanz.gwe)}}
                        </div>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.bemerkung')">Bemerkung</span>
                    </dt>
                    <dd>
                        <span>{{systeminstanz.bemerkung}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.systemtyp')">Systemtyp</span>
                    </dt>
                    <dd>
                        <div v-if="systeminstanz.systemtypId">
                            <router-link :to="{name: 'SystemtypView', params: {systemtypId: systeminstanz.systemtypId}}">{{systeminstanz.systemtypBezeichnung}}</router-link>
                        </div>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.betriebsstaette')">Betriebsstaette</span>
                    </dt>
                    <dd>
                        <div v-if="systeminstanz.betriebsstaetteId">
                            <router-link :to="{name: 'BetriebsstaetteView', params: {betriebsstaetteId: systeminstanz.betriebsstaetteId}}">{{systeminstanz.betriebsstaetteId}}</router-link>
                        </div>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systeminstanz.betreiber')">Betreiber</span>
                    </dt>
                    <dd>
                        <div v-if="systeminstanz.betreiberId">
                            <router-link :to="{name: 'BetreiberView', params: {betreiberId: systeminstanz.betreiberId}}">{{systeminstanz.betreiberId}}</router-link>
                        </div>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="systeminstanz.id" :to="{name: 'SysteminstanzEdit', params: {systeminstanzId: systeminstanz.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./systeminstanz-details.component.ts">
</script>
