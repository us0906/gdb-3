<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="hersteller">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.hersteller.detail.title')">Hersteller</span> {{hersteller.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.hersteller.bezeichnung')">Bezeichnung</span>
                    </dt>
                    <dd>
                        <span>{{hersteller.bezeichnung}}</span>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.hersteller.gueltigBis')">Gueltig Bis</span>
                    </dt>
                    <dd>
                        <span>{{hersteller.gueltigBis}}</span>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="hersteller.id" :to="{name: 'HerstellerEdit', params: {herstellerId: hersteller.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./hersteller-details.component.ts">
</script>
