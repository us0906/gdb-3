<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="betriebsstaette">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.betriebsstaette.detail.title')">Betriebsstaette</span> {{betriebsstaette.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.betriebsstaette.bsnr')">Bsnr</span>
                    </dt>
                    <dd>
                        <span>{{betriebsstaette.bsnr}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betriebsstaette.strasse')">Strasse</span>
                    </dt>
                    <dd>
                        <span>{{betriebsstaette.strasse}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betriebsstaette.hausnummer')">Hausnummer</span>
                    </dt>
                    <dd>
                        <span>{{betriebsstaette.hausnummer}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betriebsstaette.plz')">Plz</span>
                    </dt>
                    <dd>
                        <span>{{betriebsstaette.plz}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.betriebsstaette.ort')">Ort</span>
                    </dt>
                    <dd>
                        <span>{{betriebsstaette.ort}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="betriebsstaette.id" :to="{name: 'BetriebsstaetteEdit', params: {betriebsstaetteId: betriebsstaette.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./betriebsstaette-details.component.ts">
</script>
