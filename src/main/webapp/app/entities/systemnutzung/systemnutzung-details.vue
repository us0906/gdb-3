<template>
    <div class="row justify-content-center">
        <div class="col-8">
            <div v-if="systemnutzung">
                <h2 class="jh-entity-heading"><span v-text="$t('gdb3App.systemnutzung.detail.title')">Systemnutzung</span> {{systemnutzung.id}}</h2>
                <dl class="row jh-entity-details">
                    <dt>
                        <span v-text="$t('gdb3App.systemnutzung.systeminstanz')">Systeminstanz</span>
                    </dt>
                    <dd>
                        <div v-if="systemnutzung.systeminstanzId">
                            <router-link :to="{name: 'SysteminstanzView', params: {systeminstanzId: systemnutzung.systeminstanzId}}">{{systemnutzung.systeminstanzId}}</router-link>
                        </div>
                    </dd>
                    <dt>
                        <span v-text="$t('gdb3App.systemnutzung.arzt')">Arzt</span>
                    </dt>
                    <dd>
                        <div v-if="systemnutzung.arztId">
                            <router-link :to="{name: 'ArztView', params: {arztId: systemnutzung.arztId}}">{{systemnutzung.arztId}}</router-link>
                        </div>
                    </dd>
                </dl>
                <button type="submit"
                        v-on:click.prevent="previousState()"
                        class="btn btn-info">
                    <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
                </button>
                <router-link v-if="systemnutzung.id" :to="{name: 'SystemnutzungEdit', params: {systemnutzungId: systemnutzung.id}}" tag="button" class="btn btn-primary">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script lang="ts" src="./systemnutzung-details.component.ts">
</script>
